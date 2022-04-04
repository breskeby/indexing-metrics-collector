package com.breskeby.idea.plugin.indextracker

import com.breskeby.idea.plugin.indextracker.settings.IndexingTrackerSettingsConfigurable
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.options.ShowSettingsUtil
import com.intellij.openapi.project.DumbAwareAction
import org.jetbrains.annotations.Nls

class ShowSettingsAction(@Nls text: String) : DumbAwareAction(text) {
    override fun actionPerformed(e: AnActionEvent) {
        ShowSettingsUtil.getInstance().showSettingsDialog(e.project, IndexingTrackerSettingsConfigurable::class.java)
    }

}