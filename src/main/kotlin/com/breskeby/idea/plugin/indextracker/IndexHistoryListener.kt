package com.breskeby.idea.plugin.indextracker

import co.elastic.clients.elasticsearch.ElasticsearchClient
import co.elastic.clients.elasticsearch._types.ElasticsearchException
import co.elastic.clients.elasticsearch.core.IndexRequest
import co.elastic.clients.elasticsearch.indices.CreateIndexRequest
import co.elastic.clients.elasticsearch.indices.ExistsRequest
import com.breskeby.idea.plugin.indextracker.model.SimpleProjectIndexingEvent
import com.breskeby.idea.plugin.indextracker.modelbuilder.SimpleProjectIndexingEventModelBuilder
import com.breskeby.idea.plugin.indextracker.settings.IndexingTrackerSettingsState
import com.intellij.notification.Notification
import com.intellij.notification.NotificationGroup
import com.intellij.notification.NotificationGroupManager
import com.intellij.notification.NotificationType
import com.intellij.openapi.progress.runBackgroundableTask
import com.intellij.openapi.project.Project
import com.intellij.util.indexing.diagnostic.ProjectIndexingHistory
import com.intellij.util.indexing.diagnostic.ProjectIndexingHistoryListener
import com.intellij.util.indexing.diagnostic.dto.toMillis
import java.io.InputStream

private const val ES_SIMPLE_PROJECT_INDEXING_INDEX_NAME = "idea-indexing"

@org.jetbrains.annotations.ApiStatus.Internal
class IndexHistoryListener : ProjectIndexingHistoryListener {

    private val settingsState = IndexingTrackerSettingsState.getInstance()
    private val elasticsearchClientFactory = ElasticsearchClientFactory(settingsState)
    private var initialized = false

    override fun onFinishedIndexing(projectIndexingHistory: ProjectIndexingHistory) {
        val project = projectIndexingHistory.project
        if (checkBasicConfiguration(project)) {
            runBackgroundableTask("Upload indexing stats", project) {
                withWarningNotifications("Error publishing Indexing stats", project) {
                    val client = elasticsearchClientFactory.newElasticsearchClient()

                    if (initialized == false) {
                        maybeCreateIndex(project, client)
                        initialized = true
                    }

                    client.index { builder: IndexRequest.Builder<SimpleProjectIndexingEvent> ->
                        builder.index(ES_SIMPLE_PROJECT_INDEXING_INDEX_NAME)
                            .document(
                                SimpleProjectIndexingEventModelBuilder.builder()
                                    .withEnvironment()
                                    .withAnonymizedData(settingsState.anonymize)
                                    .withProjectName(project.name)
                                    .withIndexingReason(projectIndexingHistory.indexingReason)
                                    .withTotalTime(projectIndexingHistory.times.totalUpdatingTime.toMillis())
                                    .withTimestamps(
                                        projectIndexingHistory.times.updatingStart.toInstant().toEpochMilli(),
                                        projectIndexingHistory.times.updatingEnd.toInstant().toEpochMilli()
                                    ).build()
                            )
                    }
                }
            }
        }
    }

    private fun checkBasicConfiguration(project: Project): Boolean {
        return if (!settingsState.elasticsearchHost.isNullOrBlank() && settingsState.elasticsearchPort > 1) {
            true
        } else {
            sentNotification(project, "Elasticsearch connection not configured")
            false
        }
    }

    private fun maybeCreateIndex(project: Project, client: ElasticsearchClient) {
        if (!elasticsearchIndexExists(client)) {
            informAboutIndexCreating(project)
            createIndexWithPredefinedMapping(client)
        }
    }

    private fun createIndexWithPredefinedMapping(client: ElasticsearchClient) {
        val input: InputStream = this.javaClass.getResourceAsStream("/idea-indexing-mapping.json")
        client.indices().create(CreateIndexRequest.of { b: CreateIndexRequest.Builder ->
            b.index(ES_SIMPLE_PROJECT_INDEXING_INDEX_NAME).withJson(input)
        }).acknowledged()
    }

    private fun elasticsearchIndexExists(client: ElasticsearchClient) =
        client.indices().exists(
            ExistsRequest.of { b: ExistsRequest.Builder -> b.index(ES_SIMPLE_PROJECT_INDEXING_INDEX_NAME) }
        ).value()

    private fun informAboutIndexCreating(project: Project) {
        sentNotification(project) {
            it.createNotification(
                "Creating elasticsearch index $ES_SIMPLE_PROJECT_INDEXING_INDEX_NAME",
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
        notficationAction.invoke(NotificationGroupManager.getInstance().getNotificationGroup("Indexing Tracker Group"))
            .notify(project)
    }

}