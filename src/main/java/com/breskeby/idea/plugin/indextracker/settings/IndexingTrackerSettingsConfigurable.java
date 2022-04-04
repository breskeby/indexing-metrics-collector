package com.breskeby.idea.plugin.indextracker.settings;

import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;
import javax.swing.JComponent;
import java.util.Arrays;

/**
 * Provides controller functionality for application settings.
 */
public class IndexingTrackerSettingsConfigurable implements Configurable {

    private IndexingTrackerSettingsComponent mySettingsComponent;

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "Indexing Tracker";
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return mySettingsComponent.getPreferredFocusedComponent();
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        mySettingsComponent = new IndexingTrackerSettingsComponent();
        return mySettingsComponent.getPanel();
    }

    @Override
    public boolean isModified() {
        IndexingTrackerSettingsState settings = IndexingTrackerSettingsState.getInstance();
        boolean modified = !mySettingsComponent.getElasticsearchHost().equals(settings.elasticsearchHost);
        modified |= mySettingsComponent.getElasticsearchPort().equals(String.valueOf(settings.elasticsearchPort));
        modified |= mySettingsComponent.getElasticsearchUsername().equals(settings.elasticsearchUsername);
        modified |= Arrays.equals(mySettingsComponent.getElasticsearchPassword(), settings.elasticsearchPassword.toCharArray());
//        modified |= mySettingsComponent.getDetailedLogging() != settings.detailedLogging;
        modified |= mySettingsComponent.getAnonymize() != settings.anonymize;
        return modified;
    }

    @Override
    public void apply() {
        IndexingTrackerSettingsState settings = IndexingTrackerSettingsState.getInstance();
        settings.elasticsearchHost = mySettingsComponent.getElasticsearchHost();
        settings.elasticsearchPort = Integer.parseInt(mySettingsComponent.getElasticsearchPort());
        settings.elasticsearchUsername = mySettingsComponent.getElasticsearchUsername();
        settings.elasticsearchPassword = String.valueOf(mySettingsComponent.getElasticsearchPassword());
//        settings.detailedLogging = mySettingsComponent.getDetailedLogging();
        settings.anonymize = mySettingsComponent.getAnonymize();
    }

    @Override
    public void reset() {
        IndexingTrackerSettingsState settings = IndexingTrackerSettingsState.getInstance();
        mySettingsComponent.setElasticsearchHost(settings.elasticsearchHost);
        mySettingsComponent.setElasticsearchPort(String.valueOf(settings.elasticsearchPort));
        mySettingsComponent.setElasticsearchUsername(settings.elasticsearchUsername);
        mySettingsComponent.setElasticsearchPassword(String.valueOf(settings.elasticsearchPassword));
//        mySettingsComponent.setDetailedLogging(settings.detailedLogging);
        mySettingsComponent.setAnonymize(settings.anonymize);
    }

    @Override
    public void disposeUIResources() {
        mySettingsComponent = null;
    }

}