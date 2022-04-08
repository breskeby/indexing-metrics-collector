package com.breskeby.idea.plugin.isc.model

import com.intellij.openapi.util.NlsSafe
import com.intellij.util.indexing.diagnostic.TimeMillis

data class SimpleProjectIndexingEvent(
    val environment: Map<String, String>,
    val projectName: @NlsSafe String,
    val indexingReason: String?,
    val totalUpdatingTime: TimeMillis,
    val scanFilesDuration: TimeMillis,
    val indexDuration: TimeMillis,
    val updatingStart: Long,
    val updatingEnd: Long,
    val fullIndexing: Boolean,
    val interrupted: Boolean
)