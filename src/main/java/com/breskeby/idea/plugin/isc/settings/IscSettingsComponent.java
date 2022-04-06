package com.breskeby.idea.plugin.isc.settings;

import com.intellij.ui.SeparatorComponent;
import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPasswordField;
import com.intellij.ui.components.JBRadioButton;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * Supports creating and managing a {@link JPanel} for the Settings Dialog.
 */
public class IscSettingsComponent {
    private static int LEFT_INDENT = 25;

    private final JPanel myMainPanel;
    private final JBTextField elasticsearchHost = new JBTextField();
    private final JBTextField elasticsearchPort = new JBTextField();
    private final JBTextField elasticsearchUsername = new JBTextField();
    private final JBPasswordField elasticsearchPassword = new JBPasswordField();

    private final JBTextField elasticsearchAccessToken = new JBTextField();
    private final JBTextField elasticsearchApiKey = new JBTextField();
    private final JBPasswordField elasticsearchApiSecret = new JBPasswordField();


    private final JBCheckBox anonymize = new JBCheckBox("Anonymize user data");

    private final JBRadioButton noAuth = new JBRadioButton("No authentication");
    private final JBRadioButton basicAuth = new JBRadioButton("Basic Authentication");
    private final JBRadioButton accessTokenAuth = new JBRadioButton("Access Token Authentication");
    private final JBRadioButton apiKeysAuth = new JBRadioButton("API Key Authentication");
    private final ButtonGroup group = new ButtonGroup();

    private final JBLabel usernameLabel = new JBLabel("Elasticsearch username: ");
    private final JBLabel userPasswordLabel = new JBLabel("Elasticsearch password: ");
    private final JBLabel accessTokenLabel = new JBLabel("Elasticsearch Access Token: ");
    private final JBLabel apiKeyLabel = new JBLabel("Elasticsearch API Key: ");
    private final JBLabel apiSecretLabel = new JBLabel("Elasticsearch API Secret: ");

    public IscSettingsComponent() {
        anonymize.setToolTipText("hashes system user and host name before uploading");

        noAuth.addActionListener(e -> configureAuth(noAuth.getModel()));
        basicAuth.addActionListener(e -> configureAuth(basicAuth.getModel()));
        accessTokenAuth.addActionListener(e -> configureAuth(accessTokenAuth.getModel()));
        apiKeysAuth.addActionListener(e -> configureAuth(apiKeysAuth.getModel()));

        group.add(noAuth);
        group.add(basicAuth);
        group.add(accessTokenAuth);
        group.add(apiKeysAuth);

        myMainPanel = FormBuilder.createFormBuilder()
                .addLabeledComponent(new JBLabel("Elasticsearch host: "), elasticsearchHost, 1, false)
                .addLabeledComponent(new JBLabel("Elasticsearch port: "), elasticsearchPort, 1, false)
                .addComponent(anonymize, 1)
                .addSeparator(10)
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

                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();

        configureAuth(group.getSelection());
    }

    private void configureAuth(ButtonModel source) {
        if (source == noAuth.getModel()) {
            noAuth.setSelected(true);
            elasticsearchUsername.setEnabled(false);
            elasticsearchPassword.setEnabled(false);
            usernameLabel.setEnabled(false);
            userPasswordLabel.setEnabled(false);

            accessTokenLabel.setEnabled(false);
            elasticsearchAccessToken.setEnabled(false);

            apiKeyLabel.setEnabled(false);
            apiSecretLabel.setEnabled(false);
            elasticsearchApiKey.setEnabled(false);
            elasticsearchApiSecret.setEnabled(false);
        } else if (source == basicAuth.getModel()) {
            basicAuth.setSelected(true);
            elasticsearchUsername.setEnabled(true);
            elasticsearchPassword.setEnabled(true);
            usernameLabel.setEnabled(true);
            userPasswordLabel.setEnabled(true);

            accessTokenLabel.setEnabled(false);
            elasticsearchAccessToken.setEnabled(false);

            apiKeyLabel.setEnabled(false);
            apiSecretLabel.setEnabled(false);
            elasticsearchApiKey.setEnabled(false);
            elasticsearchApiSecret.setEnabled(false);
        } else if (source == accessTokenAuth.getModel()) {
            elasticsearchUsername.setEnabled(false);
            elasticsearchPassword.setEnabled(false);
            usernameLabel.setEnabled(false);
            userPasswordLabel.setEnabled(false);

            apiKeyLabel.setEnabled(false);
            apiSecretLabel.setEnabled(false);
            elasticsearchApiKey.setEnabled(false);
            elasticsearchApiSecret.setEnabled(false);

            elasticsearchAccessToken.setEnabled(true);
            accessTokenAuth.setSelected(true);
            accessTokenLabel.setEnabled(true);
            accessTokenAuth.setEnabled(true);
        } else if (source == apiKeysAuth.getModel()) {
            apiKeysAuth.setSelected(true);
            apiKeyLabel.setEnabled(true);
            apiSecretLabel.setEnabled(true);
            elasticsearchApiKey.setEnabled(true);
            elasticsearchApiSecret.setEnabled(true);

            elasticsearchUsername.setEnabled(false);
            elasticsearchPassword.setEnabled(false);
            usernameLabel.setEnabled(false);
            userPasswordLabel.setEnabled(false);

            elasticsearchAccessToken.setEnabled(false);
            accessTokenLabel.setEnabled(false);
        }
    }

    public JPanel getPanel() {
        return myMainPanel;
    }

    public JComponent getPreferredFocusedComponent() {
        return elasticsearchHost;
    }

    @NotNull
    public String getElasticsearchHost() {
        return elasticsearchHost.getText();
    }

    public void setElasticsearchHost(@NotNull String newText) {
        elasticsearchHost.setText(newText);
    }

    @NotNull
    public String getElasticsearchPort() {
        return elasticsearchPort.getText();
    }

    public void setElasticsearchPort(@NotNull String newText) {
        elasticsearchPort.setText(newText);
    }

    @NotNull
    public String getElasticsearchUsername() {
        return elasticsearchUsername.getText();
    }

    public void setElasticsearchUsername(@NotNull String newText) {
        elasticsearchUsername.setText(newText);
    }

    @NotNull
    public char[] getElasticsearchPassword() {
        return elasticsearchPassword.getPassword();
    }

    public void setElasticsearchPassword(@NotNull String newText) {
        elasticsearchPassword.setText(newText);
    }

    public boolean getAnonymize() {
        return anonymize.isSelected();
    }

    public void setAnonymize(boolean newStatus) {
        anonymize.setSelected(newStatus);
    }

    public IscSettingsState.AuthType getAuthType() {
        ButtonModel selection = group.getSelection();
        if (selection == noAuth.getModel()) {
            return IscSettingsState.AuthType.NO_AUTH;
        } else if (selection == basicAuth.getModel()) {
            return IscSettingsState.AuthType.BASIC_AUTH;
        } else if (selection == accessTokenAuth.getModel()) {
            return IscSettingsState.AuthType.ACCESS_TOKEN_AUTH;
        } else {
            return IscSettingsState.AuthType.API_KEYS_AUTH;
        }
    }

    public void setAuthType(IscSettingsState.AuthType authType) {
        switch (authType) {
            case NO_AUTH:
                configureAuth(noAuth.getModel());
                break;
            case BASIC_AUTH:
                configureAuth(basicAuth.getModel());
                break;
            case ACCESS_TOKEN_AUTH:
                configureAuth(accessTokenAuth.getModel());
                break;
            case API_KEYS_AUTH:
                configureAuth(apiKeysAuth.getModel());
                break;
        }
    }


    public String getElasticsearchAccessToken() {
        return elasticsearchAccessToken.getText();
    }

    public String getElasticsearchApiKey() {
        return elasticsearchApiKey.getText();
    }

    public char[] getElasticsearchApiSecret() {
        return elasticsearchApiSecret.getPassword();
    }

    public void setElasticsearchAccessToken(String text) {
        elasticsearchAccessToken.setText(text);
    }

    public void setElasticsearchApiKey(String text) {
        elasticsearchApiKey.setText(text);
    }

    public void setElasticsearchApiSecret(String text) {
        elasticsearchApiSecret.setText(text);
    }
}