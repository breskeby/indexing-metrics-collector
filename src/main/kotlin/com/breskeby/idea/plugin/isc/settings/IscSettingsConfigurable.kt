package com.breskeby.idea.plugin.isc.settings

import com.intellij.openapi.options.Configurable
import org.jetbrains.annotations.Nls
import java.util.*
import javax.swing.JComponent


/**
 * Provides controller functionality for application settings.
 */
class IscSettingsConfigurable : Configurable {

    private var mySettingsComponent: IscSettingsComponent? = null

    @Nls(capitalization = Nls.Capitalization.Title)
    override fun getDisplayName(): String? {
        return "Indexing Tracker"
    }

    override fun getPreferredFocusedComponent(): JComponent? {
        return mySettingsComponent?.preferredFocusedComponent
    }

    override fun createComponent(): JComponent? {
        mySettingsComponent = IscSettingsComponent()
        return mySettingsComponent?.panel
    }

    override fun isModified(): Boolean {
        val settings = IscSettingsState.instance
        var modified = mySettingsComponent?.getElasticsearchIndex() != settings.elasticsearchIndex
        modified = modified or (mySettingsComponent?.getElasticsearchHost() == settings.elasticsearchHost)
        modified = modified or (mySettingsComponent?.getElasticsearchPort() == settings.elasticsearchPort.toString())
        modified = modified or (mySettingsComponent?.getElasticsearchUsername()== settings.elasticsearchUsername)
        modified = modified or (mySettingsComponent?.getElasticsearchAccessToken() == settings.elasticsearchAccessToken)
        modified = modified or (mySettingsComponent?.getElasticsearchApiKey() == settings.elasticsearchApiKey)
        modified = modified or (Arrays.equals(mySettingsComponent?.getElasticsearchApiSecret(), settings.elasticsearchApiSecret.toCharArray()))
        modified = modified or (mySettingsComponent?.authType == settings.authType)
        modified = modified or Arrays.equals(mySettingsComponent?.getElasticsearchPassword(),
            settings.elasticsearchPassword.toCharArray()
        )
        modified = modified or (mySettingsComponent?.getAnonymize() != settings.anonymize)
        return modified
    }

    override fun apply() {
        val settings = IscSettingsState.instance
        val component = mySettingsComponent!!
        settings.elasticsearchIndex = component.getElasticsearchIndex()
        settings.elasticsearchHost = component.getElasticsearchHost()
        settings.elasticsearchPort = component.getElasticsearchPort().toInt()
        settings.elasticsearchUsername = component.getElasticsearchUsername()
        settings.elasticsearchPassword = String(component.getElasticsearchPassword())
        settings.elasticsearchAccessToken = component.getElasticsearchAccessToken()
        settings.elasticsearchApiKey = component.getElasticsearchApiKey()
        settings.elasticsearchApiSecret = String(component.getElasticsearchApiSecret())
        settings.authType = component.authType
        settings.anonymize = component.getAnonymize()
    }

    override fun reset() {
        val settings = IscSettingsState.instance
        val component = mySettingsComponent!!

        component.setElasticsearchIndex(settings.elasticsearchIndex)
        component.setElasticsearchHost(settings.elasticsearchHost)
        component.setElasticsearchPort(settings.elasticsearchPort.toString())
        component.setElasticsearchUsername(settings.elasticsearchUsername)
        component.setElasticsearchPassword(settings.elasticsearchPassword)
        component.setElasticsearchAccessToken(settings.elasticsearchAccessToken)
        component.setElasticsearchApiKey(settings.elasticsearchApiKey)
        component.setElasticsearchApiSecret(settings.elasticsearchApiSecret)
        component.authType = settings.authType
        component.setAnonymize(settings.anonymize)
    }

    override fun disposeUIResources() {
        mySettingsComponent = null
    }
}