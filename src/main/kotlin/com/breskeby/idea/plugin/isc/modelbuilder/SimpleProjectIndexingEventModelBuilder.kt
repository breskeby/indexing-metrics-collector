package com.breskeby.idea.plugin.isc.modelbuilder

import com.breskeby.idea.plugin.isc.model.PerIndexerEvent
import com.breskeby.idea.plugin.isc.model.ProviderStats
import com.breskeby.idea.plugin.isc.model.IndexedFile
import com.breskeby.idea.plugin.isc.model.SimpleProjectIndexingEvent
import com.intellij.openapi.util.NlsSafe
import com.intellij.util.indexing.diagnostic.StatsPerIndexer
import com.intellij.util.indexing.diagnostic.TimeMillis
import com.intellij.util.indexing.diagnostic.dto.JsonFileProviderIndexStatistics
import com.intellij.util.indexing.diagnostic.dto.toMillis

class SimpleProjectIndexingEventModelBuilder(private val environmentBuilder: EnvironmentBuilder) {

    private lateinit var providerStatistics: List<ProviderStats>
    private lateinit var perIndexer: List<PerIndexerEvent>
    private lateinit var projectName: @NlsSafe String
    private var indexingReason: String? = null
    private var totalUpdatingTime: TimeMillis = -1
    private var indexDuration: TimeMillis = -1
    private var scanFilesDuration: TimeMillis = -1
    private var updatingStart: Long = -1
    private var updatingEnd: Long = -1
    private var wasFullIndexing: Boolean = false
    private var wasInterrupted: Boolean = false

    companion object {
        fun builder(): SimpleProjectIndexingEventModelBuilder = SimpleProjectIndexingEventModelBuilder(
            EnvironmentBuilder()
        )
    }

    // validate properties before building for proper error messages
    fun build(): SimpleProjectIndexingEvent {
        environmentBuilder.build()
        return SimpleProjectIndexingEvent(
            environmentBuilder.build(),
            projectName,
            indexingReason,
            totalUpdatingTime,
            scanFilesDuration,
            indexDuration,
            updatingStart,
            updatingEnd,
            wasFullIndexing,
            wasInterrupted,
            perIndexer,
            providerStatistics
        )
    }

    fun withAnonymizedData(anon: Boolean = true): SimpleProjectIndexingEventModelBuilder {
        environmentBuilder.withAnonymizedData(anon)
        return this
    }

    fun withEnvironment(pluginVersion: String): SimpleProjectIndexingEventModelBuilder {
        environmentBuilder.withEnvironment().withPluginVersion(pluginVersion)
        return this
    }

    fun withIndexingReason(indexingReason: String?): SimpleProjectIndexingEventModelBuilder {
        this.indexingReason = indexingReason
        return this
    }

    fun withTotalUpdatingTime(updatingTime: TimeMillis): SimpleProjectIndexingEventModelBuilder {
        this.totalUpdatingTime = updatingTime
        return this
    }

    fun withScanFilesDuration(scanFilesDuration: TimeMillis): SimpleProjectIndexingEventModelBuilder {
        this.scanFilesDuration = scanFilesDuration
        return this
    }

    fun withIndexingDuration(indexDuration: TimeMillis): SimpleProjectIndexingEventModelBuilder {
        this.indexDuration = indexDuration
        return this
    }

    fun withTimestamps(updatingStart: Long, updatingEnd: Long): SimpleProjectIndexingEventModelBuilder {
        this.updatingStart = updatingStart
        this.updatingEnd = updatingEnd
        return this
    }

    fun withProjectName(name: @NlsSafe String): SimpleProjectIndexingEventModelBuilder {
        this.projectName = name
        return this
    }

    fun withWasFullIndex(wasFullIndexing: Boolean): SimpleProjectIndexingEventModelBuilder {
        this.wasFullIndexing = wasFullIndexing
        return this
    }

    fun withWasInterrupted(withWasInterrupted: Boolean): SimpleProjectIndexingEventModelBuilder {
        this.wasInterrupted = withWasInterrupted
        return this
    }

    fun withTotalStatsPerIndexer(statsPerIndexer: Map<String, StatsPerIndexer>): SimpleProjectIndexingEventModelBuilder {
        this.perIndexer = statsPerIndexer.map { (name, stats) ->
            PerIndexerEvent(
                name,
                stats.totalIndexingTimeInAllThreads.toMillis(),
                stats.totalNumberOfFiles
            )
        }
        return this
    }

    fun withProviderStatistics(providerStatistics: List<JsonFileProviderIndexStatistics>): SimpleProjectIndexingEventModelBuilder {
        this.providerStatistics = providerStatistics.map { providerStats ->
            ProviderStats(
                providerStats.providerName,
                providerStats.totalIndexingTime.milliseconds,
                providerStats.totalNumberOfIndexedFiles,
                providerStats.slowIndexedFiles.map { sif: JsonFileProviderIndexStatistics.JsonSlowIndexedFile ->
                    IndexedFile(sif.fileName,
                        sif.indexingTime.milliseconds,
                        sif.contentLoadingTime.milliseconds,
                        sif.processingTime.milliseconds)
                }
            )
        }
        return this
    }

}