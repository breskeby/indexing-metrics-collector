package com.breskeby.idea.plugin.isc.settings;

import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;
import javax.swing.JComponent;
import java.util.Arrays;

/**
 * Provides controller functionality for application settings.
 */
public class IscSettingsConfigurable implements Configurable {

    private IscSettingsComponent mySettingsComponent;
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
        mySettingsComponent = new IscSettingsComponent();
        return mySettingsComponent.getPanel();
    }

    @Override
    public boolean isModified() {
        IscSettingsState settings = IscSettingsState.getInstance();
        boolean modified = !mySettingsComponent.getElasticsearchHost().equals(settings.elasticsearchHost);
        modified |= mySettingsComponent.getElasticsearchPort().equals(String.valueOf(settings.elasticsearchPort));
        modified |= mySettingsComponent.getElasticsearchUsername().equals(settings.elasticsearchUsername);
        modified |= mySettingsComponent.getElasticsearchAccessToken().equals(settings.elasticsearchAccessToken);
        modified |= mySettingsComponent.getElasticsearchApiKey().equals(settings.elasticsearchApiKey);
        modified |= mySettingsComponent.getElasticsearchApiSecret().equals(settings.elasticsearchApiSecret);
        modified |= mySettingsComponent.getAuthType() == settings.authType;
        modified |= Arrays.equals(mySettingsComponent.getElasticsearchPassword(), settings.elasticsearchPassword.toCharArray());
        modified |= mySettingsComponent.getAnonymize() != settings.anonymize;
        return modified;
    }

    @Override
    public void apply() {
        IscSettingsState settings = IscSettingsState.getInstance();
        settings.elasticsearchHost = mySettingsComponent.getElasticsearchHost();
        settings.elasticsearchPort = Integer.parseInt(mySettingsComponent.getElasticsearchPort());
        settings.elasticsearchUsername = mySettingsComponent.getElasticsearchUsername();
        settings.elasticsearchPassword = String.valueOf(mySettingsComponent.getElasticsearchPassword());
        settings.elasticsearchAccessToken = mySettingsComponent.getElasticsearchAccessToken();
        settings.elasticsearchApiKey = mySettingsComponent.getElasticsearchApiKey();
        settings.elasticsearchApiSecret = String.valueOf(mySettingsComponent.getElasticsearchApiSecret());
        settings.authType = mySettingsComponent.getAuthType();
        settings.anonymize = mySettingsComponent.getAnonymize();
    }

    @Override
    public void reset() {
        IscSettingsState settings = IscSettingsState.getInstance();
        mySettingsComponent.setElasticsearchHost(settings.elasticsearchHost);
        mySettingsComponent.setElasticsearchPort(String.valueOf(settings.elasticsearchPort));
        mySettingsComponent.setElasticsearchUsername(settings.elasticsearchUsername);
        mySettingsComponent.setElasticsearchPassword(String.valueOf(settings.elasticsearchPassword));
        mySettingsComponent.setElasticsearchAccessToken(settings.elasticsearchAccessToken);
        mySettingsComponent.setElasticsearchApiKey(settings.elasticsearchApiKey);
        mySettingsComponent.setElasticsearchApiSecret(String.valueOf(settings.elasticsearchApiSecret));
        mySettingsComponent.setAuthType(settings.authType);
        mySettingsComponent.setAnonymize(settings.anonymize);
    }

    @Override
    public void disposeUIResources() {
        mySettingsComponent = null;
    }

}