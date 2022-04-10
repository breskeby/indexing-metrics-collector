package com.breskeby.idea.plugin.isc.model

import com.intellij.openapi.util.NlsSafe
import com.intellij.util.indexing.diagnostic.SlowIndexedFile

data class SimpleProjectIndexingEvent(
    val environment: Map<String, String>,
    val projectName: @NlsSafe String,
    val indexingReason: String?,
    val totalUpdatingTime: Long,
    val scanFilesDuration: Long,
    val indexDuration: Long,
    val updatingStart: Long,
    val updatingEnd: Long,
    val fullIndexing: Boolean,
    val interrupted: Boolean,
    val perIndexer: List<PerIndexerEvent>,
    val providerStatistics: List<ProviderStats>
)

data class PerIndexerEvent(val providerName: String, val totalIndexingTime: Long, val totalNumberOfFiles: Int)

data class ProviderStats(
    val name: String,
    val totalIndexingTime: Long,
    val totalNumberOfFiles: Int,
    val slowIndexedFiles: List<IndexedFile>
)

data class IndexedFile(
    val fileName: String,
    val processingTime: Long,
    val indexingTime: Long,
    val contentLoadingTime: Long
)