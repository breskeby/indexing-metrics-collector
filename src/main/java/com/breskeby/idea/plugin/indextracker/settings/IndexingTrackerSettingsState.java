package com.breskeby.idea.plugin.indextracker.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

/**
 * Supports storing the application settings in a persistent way.
 * The {@link State} and {@link Storage} annotations define the name of the data and the file name where
 * these persistent application settings are stored.
 */
@State(
        name = "com.breskeby.ideaindextracker.settings",
        storages = @Storage("IndexingTrackerPlugin.xml")
)
public class IndexingTrackerSettingsState implements PersistentStateComponent<IndexingTrackerSettingsState> {

    // TODO Add customizable es index name
    public String elasticsearchHost = "";
    public int elasticsearchPort = 9243;
    public String elasticsearchUsername = "";
    public String elasticsearchPassword = "";
    public boolean anonymize = true;
//    public boolean detailedLogging = false;

    public static IndexingTrackerSettingsState getInstance() {
        return ApplicationManager.getApplication().getService(IndexingTrackerSettingsState.class);
    }

    @Nullable
    @Override
    public IndexingTrackerSettingsState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull IndexingTrackerSettingsState state) {
        XmlSerializerUtil.copyBean(state, this);
    }

}