/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 * */

package co.elastic.idea.plugin.imc.modelbuilder

import co.elastic.idea.plugin.imc.model.SimpleProjectIndexingEvent
import com.intellij.openapi.util.NlsSafe
import com.intellij.util.indexing.diagnostic.TimeMillis

class ProjectIndexingEventModelBuilder(
    private val environmentBuilder: EnvironmentBuilder,
    private val platformInfoBuilder: PlatformInfoBuilder
) {

    private lateinit var projectName: @NlsSafe String
    private var indexingReason: String? = null
    private var totalUpdatingTime: TimeMillis = -1
    private var indexDuration: TimeMillis = -1
    private var scanFilesDuration: TimeMillis = -1
    private var updatingStart: Long = -1
    private var updatingEnd: Long = -1
    private var wasFullIndexing: Boolean = false
    private var wasInterrupted: Boolean = false

    fun withAnonymizedData(anon: Boolean = true): ProjectIndexingEventModelBuilder {
        environmentBuilder.withAnonymizedData(anon)
        return this
    }

    fun withEnvironment(pluginVersion: String): ProjectIndexingEventModelBuilder {
        environmentBuilder.withEnvironment().withPluginVersion(pluginVersion)
        return this
    }

    fun withIndexingReason(indexingReason: String?): ProjectIndexingEventModelBuilder {
        this.indexingReason = indexingReason
        return this
    }

    fun withTotalUpdatingTime(updatingTime: TimeMillis): ProjectIndexingEventModelBuilder {
        this.totalUpdatingTime = updatingTime
        return this
    }

    fun withScanFilesDuration(scanFilesDuration: TimeMillis): ProjectIndexingEventModelBuilder {
        this.scanFilesDuration = scanFilesDuration
        return this
    }

    fun withIndexingDuration(indexDuration: TimeMillis): ProjectIndexingEventModelBuilder {
        this.indexDuration = indexDuration
        return this
    }

    fun withTimestamps(updatingStart: Long, updatingEnd: Long): ProjectIndexingEventModelBuilder {
        this.updatingStart = updatingStart
        this.updatingEnd = updatingEnd
        return this
    }

    fun withProjectName(name: String): ProjectIndexingEventModelBuilder {
        this.projectName = name
        return this
    }

    fun withWasFullIndex(wasFullIndexing: Boolean): ProjectIndexingEventModelBuilder {
        this.wasFullIndexing = wasFullIndexing
        return this
    }

    fun withWasInterrupted(withWasInterrupted: Boolean): ProjectIndexingEventModelBuilder {
        this.wasInterrupted = withWasInterrupted
        return this
    }

    // validate properties before building for proper error messages
    fun build(): SimpleProjectIndexingEvent {
        environmentBuilder.build()
        return SimpleProjectIndexingEvent(
            environmentBuilder.build(),
            platformInfoBuilder.build(),
            projectName,
            indexingReason,
            totalUpdatingTime,
            scanFilesDuration,
            indexDuration,
            updatingStart,
            updatingEnd,
            wasFullIndexing,
            wasInterrupted
        )
    }

    companion object {
        fun builder(): ProjectIndexingEventModelBuilder = ProjectIndexingEventModelBuilder(
            EnvironmentBuilder(),
            PlatformInfoBuilder()
        )
    }

}
