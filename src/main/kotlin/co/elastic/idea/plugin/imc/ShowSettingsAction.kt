package co.elastic.idea.plugin.imc

import co.elastic.idea.plugin.imc.settings.ImcSettingsConfigurable
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.DumbAwareAction
import org.jetbrains.annotations.Nls

class ShowSettingsAction(@Nls text: String) : DumbAwareAction(text) {
    override fun actionPerformed(e: AnActionEvent) {
        ShowSettingsUtil.getInstance().showSettingsDialog(e.project, ImcSettingsConfigurable::class.java)
    }
}
