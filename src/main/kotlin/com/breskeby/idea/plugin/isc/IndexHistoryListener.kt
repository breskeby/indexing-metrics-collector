package com.breskeby.idea.plugin.isc

import co.elastic.clients.elasticsearch.ElasticsearchClient
import co.elastic.clients.elasticsearch._types.ElasticsearchException
import co.elastic.clients.elasticsearch.core.IndexRequest
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest
import co.elastic.clients.elasticsearch.indices.ExistsRequest
import com.breskeby.idea.plugin.isc.model.SimpleProjectIndexingEvent
import com.breskeby.idea.plugin.isc.modelbuilder.SimpleProjectIndexingEventModelBuilder
import com.breskeby.idea.plugin.isc.settings.IscSettingsState
import com.intellij.ide.plugins.PluginManager
import com.intellij.notification.Notification
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.extensions.PluginId
import com.intellij.openapi.progress.runBackgroundableTask
import com.intellij.openapi.project.Project
import com.intellij.util.indexing.diagnostic.ProjectIndexingHistory
import com.intellij.util.indexing.diagnostic.ProjectIndexingHistoryListener
import com.intellij.util.indexing.diagnostic.dto.toMillis
import java.io.InputStream

@org.jetbrains.annotations.ApiStatus.Internal
class IndexHistoryListener : ProjectIndexingHistoryListener {

    private val settingsState = IscSettingsState.instance
    private val elasticsearchClientFactory = ElasticsearchClientFactory(settingsState)
    private val version = PluginManager.getInstance().findEnabledPlugin(
        PluginId.getId("com.github.breskeby.idea.indexingstatscollector")
    )!!.version

    private var initialized = false

    override fun onFinishedIndexing(projectIndexingHistory: ProjectIndexingHistory) {
        val project = projectIndexingHistory.project
        runBackgroundableTask("Uploading indexing stats", project) {
            if (validBasicConfiguration(project)) {
                val index = settingsState.elasticsearchIndex
                withWarningNotifications("Error publishing Indexing stats", project) {
                    val client = elasticsearchClientFactory.newElasticsearchClient()
                    maybeInitializeIndex(project, client, index)
                    client.index { builder: IndexRequest.Builder<SimpleProjectIndexingEvent> ->
                        builder.index(index)
                            .document(
                                SimpleProjectIndexingEventModelBuilder.builder()
                                    .withEnvironment(version)
                                    .withAnonymizedData(settingsState.anonymize)
                                    .withProjectName(project.name)
                                    .withIndexingReason(projectIndexingHistory.indexingReason)
                                    .withWasFullIndex(projectIndexingHistory.times.wasFullIndexing)
                                    .withWasInterrupted(projectIndexingHistory.times.wasInterrupted)
                                    .withScanFilesDuration(projectIndexingHistory.times.scanFilesDuration.toMillis())
                                    .withScanFilesDuration(projectIndexingHistory.times.scanFilesDuration.toMillis())
                                    .withTotalUpdatingTime(projectIndexingHistory.times.totalUpdatingTime.toMillis())
                                    .withIndexingDuration(projectIndexingHistory.times.indexingDuration.toMillis())
                                    .withTimestamps(
                                        projectIndexingHistory.times.updatingStart.toInstant().toEpochMilli(),
                                        projectIndexingHistory.times.updatingEnd.toInstant().toEpochMilli()
                                    )
                                    .withTotalStatsPerIndexer(projectIndexingHistory.totalStatsPerIndexer)
                                    .withProviderStatistics(projectIndexingHistory.providerStatistics)
                                    .build()
                            )
                    }
                }
            }
        }
    }

    private fun maybeInitializeIndex(
        project: Project,
        client: ElasticsearchClient,
        index: String
    ) {
        if (initialized == false) {
            maybeCreateIndex(project, client, index)
            initialized = true
        }
    }

    private fun validBasicConfiguration(project: Project): Boolean {
        return if (!settingsState.elasticsearchHost.isNullOrBlank() && settingsState.elasticsearchPort > 1) {
            true
        } else {
            sentNotification(project, "Indexing stats collector endpoint not configured")
            false
        }
    }

    private fun maybeCreateIndex(project: Project, client: ElasticsearchClient, index: String) {
        if (!elasticsearchIndexExists(client, index)) {
            informAboutIndexCreating(project, index)
            createIndexWithPredefinedMapping(client, index)
        }
    }

    private fun createIndexWithPredefinedMapping(client: ElasticsearchClient, index: String) {
        val input: InputStream = this.javaClass.getResourceAsStream("/idea-indexing-mapping.json")
        client.indices().create(CreateIndexRequest.of { b: CreateIndexRequest.Builder ->
            b.index(index).withJson(input)
        }).acknowledged()
    }

    private fun elasticsearchIndexExists(client: ElasticsearchClient, index: String) =
        client.indices().exists(
            ExistsRequest.of { b: ExistsRequest.Builder -> b.index(index) }
        ).value()

    private fun informAboutIndexCreating(project: Project, index: String) {
        sentNotification(project) {
            it.createNotification(
                "Creating indexing stats elasticsearch index $index",
                NotificationType.INFORMATION
            )
        }
    }

    private fun withWarningNotifications(content: String, project: Project, action: () -> Unit) {
        try {
            action.invoke()
        } catch (e: ElasticsearchException) {
            sentNotification(project, content)
        }
    }

    private fun sentNotification(project: Project, content: String) {
        sentNotification(project) {
            it.createNotification(content, NotificationType.WARNING).addAction(ShowSettingsAction("Configure..."))
        }
    }

    private fun sentNotification(project: Project, notficationAction: (group: NotificationGroup) -> Notification) {
        notficationAction.invoke(NotificationGroupManager.getInstance().getNotificationGroup("Indexing Stats Collector Group"))
            .notify(project)
    }

}