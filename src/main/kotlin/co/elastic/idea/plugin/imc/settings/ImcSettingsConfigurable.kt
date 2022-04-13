package co.elastic.idea.plugin.imc.settings

import com.intellij.openapi.options.Configurable
import org.jetbrains.annotations.Nls
import java.util.*
import javax.swing.JComponent

/**
 * Provides controller functionality for application settings.
 */
class ImcSettingsConfigurable : Configurable {

    private var mySettingsComponent: ImcSettingsComponent? = null

    @Nls(capitalization = Nls.Capitalization.Title)
    override fun getDisplayName(): String? {
        return "Indexing Metrics Collector"
    }

    override fun getPreferredFocusedComponent(): JComponent? {
        return mySettingsComponent?.preferredFocusedComponent
    }

    override fun createComponent(): JComponent? {
        mySettingsComponent = ImcSettingsComponent()
        return mySettingsComponent?.panel
    }

    override fun isModified(): Boolean {
        val settings = ImcSettingsState.instance
        var modified = mySettingsComponent?.getElasticsearchIndex() != settings.esSearcIndex
        modified = modified or (mySettingsComponent?.getElasticsearchHost() == settings.esHost)
        modified = modified or (mySettingsComponent?.getElasticsearchPort() == settings.esPort.toString())
        modified = modified or (mySettingsComponent?.getElasticsearchUsername()== settings.esUsername)
        modified = modified or (mySettingsComponent?.getElasticsearchAccessToken() == settings.esAccessToken)
        modified = modified or (mySettingsComponent?.getElasticsearchApiKey() == settings.esApiKey)
        modified = modified or (Arrays.equals(mySettingsComponent?.getElasticsearchApiSecret(),
            settings.esApiSecret.toCharArray()))
        modified = modified or (mySettingsComponent?.authType == settings.authType)
        modified = modified or Arrays.equals(mySettingsComponent?.getElasticsearchPassword(),
            settings.esPassword.toCharArray()
        )
        modified = modified or (mySettingsComponent?.getAnonymize() != settings.anonymize)
        return modified
    }

    override fun apply() {
        val settings = ImcSettingsState.instance
        val component = mySettingsComponent!!
        settings.esSearcIndex = component.getElasticsearchIndex()
        settings.esHost = component.getElasticsearchHost()
        settings.esPort = component.getElasticsearchPort().toInt()
        settings.esUsername = component.getElasticsearchUsername()
        settings.esPassword = String(component.getElasticsearchPassword())
        settings.esAccessToken = component.getElasticsearchAccessToken()
        settings.esApiKey = component.getElasticsearchApiKey()
        settings.esApiSecret = String(component.getElasticsearchApiSecret())
        settings.authType = component.authType
        settings.anonymize = component.getAnonymize()
    }

    override fun reset() {
        val settings = ImcSettingsState.instance
        val component = mySettingsComponent!!

        component.setElasticsearchIndex(settings.esSearcIndex)
        component.setElasticsearchHost(settings.esHost)
        component.setElasticsearchPort(settings.esPort.toString())
        component.setElasticsearchUsername(settings.esUsername)
        component.setElasticsearchPassword(settings.esPassword)
        component.setElasticsearchAccessToken(settings.esAccessToken)
        component.setElasticsearchApiKey(settings.esApiKey)
        component.setElasticsearchApiSecret(settings.esApiSecret)
        component.authType = settings.authType
        component.setAnonymize(settings.anonymize)
    }

    override fun disposeUIResources() {
        mySettingsComponent = null
    }
}
