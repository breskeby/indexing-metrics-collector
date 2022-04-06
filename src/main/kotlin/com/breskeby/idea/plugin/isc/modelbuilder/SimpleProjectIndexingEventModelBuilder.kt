package com.breskeby.idea.plugin.isc.modelbuilder

import com.breskeby.idea.plugin.isc.model.SimpleProjectIndexingEvent
import com.intellij.openapi.util.NlsSafe
import com.intellij.util.indexing.diagnostic.TimeMillis

class SimpleProjectIndexingEventModelBuilder(private val environmentBuilder: EnvironmentBuilder) {

    private lateinit var projectName: @NlsSafe String
    private var indexingReason: String? = null
    private var totalUpdatingTime: TimeMillis = -1
    private var updatingStart: Long = -1
    private var updatingEnd: Long = -1

    companion object {
        fun builder() : SimpleProjectIndexingEventModelBuilder = SimpleProjectIndexingEventModelBuilder(
            EnvironmentBuilder()
        )
    }

    fun withAnonymizedData(anon: Boolean = true) : SimpleProjectIndexingEventModelBuilder {
        environmentBuilder.withAnonymizedData(anon)
        return this
    }

    fun withEnvironment() : SimpleProjectIndexingEventModelBuilder {
        environmentBuilder.withEnvironment()
        return this
    }

    fun withIndexingReason(indexingReason: String?) : SimpleProjectIndexingEventModelBuilder {
        this.indexingReason = indexingReason
        return this
    }

    fun withTotalTime(updatingTime:TimeMillis) : SimpleProjectIndexingEventModelBuilder {
        this.totalUpdatingTime = updatingTime
        return this
    }

    fun withTimestamps(updatingStart:Long, updatingEnd: Long) : SimpleProjectIndexingEventModelBuilder {
        this.updatingStart = updatingStart
        this.updatingEnd = updatingEnd
        return this
    }

    fun withProjectName(name: @NlsSafe String): SimpleProjectIndexingEventModelBuilder {
        this.projectName = name
        return this
    }
    // validate properties before building for proper error messages
    fun build() : SimpleProjectIndexingEvent {
        environmentBuilder.build()
        return SimpleProjectIndexingEvent(environmentBuilder.build(),
        projectName,
        indexingReason,
        totalUpdatingTime,
        updatingStart,
        updatingEnd)
    }


}