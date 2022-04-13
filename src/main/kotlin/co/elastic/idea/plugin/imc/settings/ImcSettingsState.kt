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

package co.elastic.idea.plugin.imc.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil

/**
 * Supports storing the application settings in a persistent way.
 * The [State] and [Storage] annotations define the name of the data and the file name where
 * these persistent application settings are stored.
 */
@State(name = "co.elastic.indexingmetricscollector.settings",
    storages = [Storage("IndexingMetricsCollectorPlugin.xml")]
)
class ImcSettingsState : PersistentStateComponent<ImcSettingsState?> {
    var esSearcIndex = "idea-indexing"
    var esHost = ""
    var esPort = 9243
    var anonymize = true
    var esUsername = ""
    var esPassword = ""
    var esAccessToken = ""
    var esApiKey = ""
    var esApiSecret = ""
    var authType = AuthType.NO_AUTH
    override fun getState(): ImcSettingsState? {
        return this
    }

    override fun loadState(state: ImcSettingsState) {
        XmlSerializerUtil.copyBean(state, this)
    }

    enum class AuthType {
        NO_AUTH, BASIC_AUTH, ACCESS_TOKEN_AUTH, API_KEYS_AUTH
    }

    companion object {
        val instance: ImcSettingsState
            get() = ApplicationManager.getApplication().getService(ImcSettingsState::class.java)
    }
}
