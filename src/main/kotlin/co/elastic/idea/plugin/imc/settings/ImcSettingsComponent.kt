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

import co.elastic.clients.elasticsearch._types.ElasticsearchException
import co.elastic.idea.plugin.imc.elasticsearch.ElasticsearchClientFactory
import co.elastic.idea.plugin.imc.elasticsearch.ElasticsearchConnectionDetails
import co.elastic.idea.plugin.imc.elasticsearch.ElasticsearchConnectionDetails.AuthType
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.ui.Messages
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBPasswordField
import com.intellij.ui.components.JBRadioButton
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import java.io.IOException
import java.util.concurrent.atomic.AtomicReference
import javax.swing.ButtonGroup
import javax.swing.ButtonModel
import javax.swing.JButton
import javax.swing.JComponent
import javax.swing.JPanel

/**
 * Supports creating and managing a [JPanel] for the Settings Dialog.
 */
class ImcSettingsComponent {
    val panel: JPanel
    private val elasticsearchIndex = JBTextField()
    private val elasticsearchHost = JBTextField()
    private val elasticsearchPort = JBTextField()
    private val elasticsearchUsername = JBTextField()
    private val elasticsearchPassword = JBPasswordField()
    private val elasticsearchAccessToken = JBTextField()
    private val elasticsearchApiKey = JBTextField()
    private val elasticsearchApiSecret = JBPasswordField()
    private val anonymize = JBCheckBox("Anonymize user data")
    private val noAuth = JBRadioButton("No authentication")
    private val basicAuth = JBRadioButton("Basic Authentication")
    private val accessTokenAuth = JBRadioButton("Access Token Authentication")
    private val apiKeysAuth = JBRadioButton("API Key Authentication")
    private val group = ButtonGroup()
    private val usernameLabel = JBLabel("Elasticsearch username: ")
    private val userPasswordLabel = JBLabel("Elasticsearch password: ")
    private val accessTokenLabel = JBLabel("Elasticsearch Access Token: ")
    private val apiKeyLabel = JBLabel("Elasticsearch API Key: ")
    private val apiSecretLabel = JBLabel("Elasticsearch API Secret: ")
    private val testConnectionButton = JButton("Check connection")


    init {
        anonymize.toolTipText = "hashes system user and host name before uploading"
        noAuth.addActionListener { configureAuth(noAuth.model) }
        basicAuth.addActionListener { configureAuth(basicAuth.model) }
        accessTokenAuth.addActionListener { configureAuth(accessTokenAuth.model) }
        apiKeysAuth.addActionListener { configureAuth(apiKeysAuth.model) }
        testConnectionButton.addActionListener { testConnection() }
        group.add(noAuth)
        group.add(basicAuth)
        group.add(accessTokenAuth)
        group.add(apiKeysAuth)


        panel = FormBuilder.createFormBuilder()
            .addLabeledComponent(JBLabel("Elasticsearch index: "), elasticsearchIndex, 1, false)
            .addLabeledComponent(JBLabel("Elasticsearch host: "), elasticsearchHost, 1, false)
            .addLabeledComponent(JBLabel("Elasticsearch port: "), elasticsearchPort, 1, false)
            .addComponent(anonymize, 1)
            .addSeparator(SEPARATOR_INSET)
            .addComponent(noAuth, 1)
            .addComponent(basicAuth, 1)
            .setFormLeftIndent(LEFT_INDENT)
            .addLabeledComponent(usernameLabel, elasticsearchUsername, 1, false)
            .addLabeledComponent(userPasswordLabel, elasticsearchPassword, 1, false)
            .setFormLeftIndent(0)
            .addComponent(accessTokenAuth, 1)
            .setFormLeftIndent(LEFT_INDENT)
            .addLabeledComponent(accessTokenLabel, elasticsearchAccessToken, 1, false)
            .setFormLeftIndent(0)
            .addComponent(apiKeysAuth, 1)
            .setFormLeftIndent(LEFT_INDENT)
            .addLabeledComponent(apiKeyLabel, elasticsearchApiKey, 1, false)
            .addLabeledComponent(apiSecretLabel, elasticsearchApiSecret, 1, false)
            .addComponent(testConnectionButton, 1)
            .addComponentFillVertically(JPanel(), 0)
            .panel

        configureAuth(if (group.selection == null) noAuth.model else group.selection)
    }

    var authType: ImcSettingsState.AuthType
        get() {
            val selection = group.selection
            return if (selection === noAuth.model) {
                ImcSettingsState.AuthType.NO_AUTH
            } else if (selection === basicAuth.model) {
                ImcSettingsState.AuthType.BASIC_AUTH
            } else if (selection === accessTokenAuth.model) {
                ImcSettingsState.AuthType.ACCESS_TOKEN_AUTH
            } else {
                ImcSettingsState.AuthType.API_KEYS_AUTH
            }
        }
        set(authType) {
            when (authType) {
                ImcSettingsState.AuthType.NO_AUTH -> configureAuth(noAuth.model)
                ImcSettingsState.AuthType.BASIC_AUTH -> configureAuth(basicAuth.model)
                ImcSettingsState.AuthType.ACCESS_TOKEN_AUTH -> configureAuth(accessTokenAuth.model)
                ImcSettingsState.AuthType.API_KEYS_AUTH -> configureAuth(apiKeysAuth.model)
            }
        }

    val preferredFocusedComponent: JComponent
        get() = elasticsearchHost

    private fun configureAuth(source: ButtonModel) {
        if (source === noAuth.model) {
            noAuth.isSelected = true
            elasticsearchUsername.isEnabled = false
            elasticsearchPassword.isEnabled = false
            usernameLabel.isEnabled = false
            userPasswordLabel.isEnabled = false
            accessTokenLabel.isEnabled = false
            elasticsearchAccessToken.isEnabled = false
            apiKeyLabel.isEnabled = false
            apiSecretLabel.isEnabled = false
            elasticsearchApiKey.isEnabled = false
            elasticsearchApiSecret.isEnabled = false
        } else if (source === basicAuth.model) {
            basicAuth.isSelected = true
            elasticsearchUsername.isEnabled = true
            elasticsearchPassword.isEnabled = true
            usernameLabel.isEnabled = true
            userPasswordLabel.isEnabled = true
            accessTokenLabel.isEnabled = false
            elasticsearchAccessToken.isEnabled = false
            apiKeyLabel.isEnabled = false
            apiSecretLabel.isEnabled = false
            elasticsearchApiKey.isEnabled = false
            elasticsearchApiSecret.isEnabled = false
        } else if (source === accessTokenAuth.model) {
            elasticsearchUsername.isEnabled = false
            elasticsearchPassword.isEnabled = false
            usernameLabel.isEnabled = false
            userPasswordLabel.isEnabled = false
            apiKeyLabel.isEnabled = false
            apiSecretLabel.isEnabled = false
            elasticsearchApiKey.isEnabled = false
            elasticsearchApiSecret.isEnabled = false
            elasticsearchAccessToken.isEnabled = true
            accessTokenAuth.isSelected = true
            accessTokenLabel.isEnabled = true
            accessTokenAuth.isEnabled = true
        } else if (source === apiKeysAuth.model) {
            apiKeysAuth.isSelected = true
            apiKeyLabel.isEnabled = true
            apiSecretLabel.isEnabled = true
            elasticsearchApiKey.isEnabled = true
            elasticsearchApiSecret.isEnabled = true
            elasticsearchUsername.isEnabled = false
            elasticsearchPassword.isEnabled = false
            usernameLabel.isEnabled = false
            userPasswordLabel.isEnabled = false
            elasticsearchAccessToken.isEnabled = false
            accessTokenLabel.isEnabled = false
        }
    }

    fun getElasticsearchHost(): String {
        return elasticsearchHost.text
    }

    fun setElasticsearchHost(newText: String) {
        elasticsearchHost.text = newText
    }

    fun getElasticsearchPort(): String {
        return elasticsearchPort.text
    }

    fun setElasticsearchPort(newText: String) {
        elasticsearchPort.text = newText
    }

    fun getElasticsearchIndex(): String {
        return elasticsearchIndex.text
    }

    fun setElasticsearchIndex(newText: String) {
        elasticsearchIndex.text = newText
    }

    fun getElasticsearchUsername(): String {
        return elasticsearchUsername.text
    }

    fun setElasticsearchUsername(newText: String) {
        elasticsearchUsername.text = newText
    }

    fun getElasticsearchPassword(): CharArray {
        return elasticsearchPassword.password
    }

    fun setElasticsearchPassword(newText: String) {
        elasticsearchPassword.text = newText
    }

    fun getAnonymize(): Boolean {
        return anonymize.isSelected
    }

    fun setAnonymize(newStatus: Boolean) {
        anonymize.isSelected = newStatus
    }

    fun getElasticsearchAccessToken(): String {
        return elasticsearchAccessToken.text
    }

    fun getElasticsearchApiKey(): String {
        return elasticsearchApiKey.text
    }

    fun getElasticsearchApiSecret(): CharArray {
        return elasticsearchApiSecret.password
    }

    fun setElasticsearchAccessToken(text: String?) {
        elasticsearchAccessToken.text = text
    }

    fun setElasticsearchApiKey(text: String?) {
        elasticsearchApiKey.text = text
    }

    fun setElasticsearchApiSecret(text: String?) {
        elasticsearchApiSecret.text = text
    }


    private fun testConnection() {
        val connectionDetails = object : ElasticsearchConnectionDetails {
            override fun getIndex(): String = elasticsearchIndex.text

            override fun getHost(): String = elasticsearchHost.text

            override fun getPort(): Int = Integer.parseInt(elasticsearchPort.text)

            override fun getAuthType(): AuthType = AuthType.valueOf(authType.name);

            override fun getUsername(): String = elasticsearchUsername.text

            override fun getPassword(): String = String(elasticsearchPassword.password)

            override fun getAccessToken(): String = elasticsearchAccessToken.text

            override fun getApiKey(): String = elasticsearchApiKey.text

            override fun getApiSecret(): String = String(elasticsearchApiSecret.password)
        }

        val exceptionReference = AtomicReference<Exception>()
        val indexExists = AtomicReference<Boolean>()
        ProgressManager.getInstance().runProcessWithProgressSynchronously({
            try {
                val elasticsearchClientFactory = ElasticsearchClientFactory(connectionDetails)
                indexExists.set(elasticsearchClientFactory.checkConnection(elasticsearchIndex.text))
            } catch (e: IOException) {
                exceptionReference.set(e)
            } catch (e: ElasticsearchException) {
                exceptionReference.set(e)
            }
        }, "Checking Elasticsearch Connection...", true, null)

        if(exceptionReference.get() == null) {
            if(indexExists.get()) {
                Messages.showMessageDialog(
                    panel,
                    "Connection succesful",
                    "Elasticsearch connection check",
                    Messages.getInformationIcon()
                )
            } else {
                Messages.showMessageDialog(
                    panel,
                    "Index '${connectionDetails.getIndex()}' does not exist (yet)",
                    "Elasticsearch connection check",
                    Messages.getWarningIcon()
                )
            }
        } else {
            Messages.showErrorDialog(panel, exceptionReference.get().message)
        }

    }

    companion object {
        private const val LEFT_INDENT = 25
        private const val SEPARATOR_INSET = 10
    }
}
