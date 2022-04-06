package com.breskeby.idea.plugin.isc.model

import com.intellij.openapi.util.NlsSafe
import com.intellij.util.indexing.diagnostic.TimeMillis

data class SimpleProjectIndexingEvent(
    val environment: Map<String, String>,
    val projectName: @NlsSafe String,
    val indexingReason: String?,
    val totalUpdatingTime: TimeMillis,
    val updatingStart: Long,
    val updatingEnd: Long
)