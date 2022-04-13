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
@State(name = "co.elastic.indexingmetricscollector.settings", storages = [Storage("IndexingMetricsCollectorPlugin.xml")])
class ImcSettingsState : PersistentStateComponent<ImcSettingsState?> {
    var elasticsearchIndex = "idea-indexing"
    var elasticsearchHost = ""
    var elasticsearchPort = 9243
    var anonymize = true
    var elasticsearchUsername = ""
    var elasticsearchPassword = ""
    var elasticsearchAccessToken = ""
    var elasticsearchApiKey = ""
    var elasticsearchApiSecret = ""
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
