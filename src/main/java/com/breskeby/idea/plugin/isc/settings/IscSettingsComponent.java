package com.breskeby.idea.plugin.isc.settings;

import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPasswordField;
import com.intellij.ui.components.JBRadioButton;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.UIUtil;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.ActionListener;

/**
 * Supports creating and managing a {@link JPanel} for the Settings Dialog.
 */
public class IscSettingsComponent {

    private final JPanel myMainPanel;
    private final JBTextField elasticsearchHost = new JBTextField();
    private final JBTextField elasticsearchPort = new JBTextField();
    private final JBTextField elasticsearchUsername = new JBTextField();
    private final JBLabel usernameLabel = new JBLabel("Elasticsearch username: ");
    private final JBLabel userPasswordLabel = new JBLabel("Elasticsearch password: ");

    private final JBPasswordField elasticsearchPassword = new JBPasswordField();

    private final JBCheckBox detailedLogging = new JBCheckBox("Detailed logging");
    private final JBCheckBox anonymize = new JBCheckBox("Anonymize user data");

    private final JBRadioButton noAuth = new JBRadioButton("No authentication");
    private final JBRadioButton basicAuth = new JBRadioButton("Basic Authentication");

    public IscSettingsComponent() {
        ButtonGroup group = new ButtonGroup();

        noAuth.addActionListener(e -> disableAuthentication(noAuth.isSelected()));
        basicAuth.addActionListener(e -> disableAuthentication(noAuth.isSelected()));
        group.add(noAuth);
        group.add(basicAuth);

        myMainPanel = FormBuilder.createFormBuilder()

                .addLabeledComponent(new JBLabel("Elasticsearch host: "), elasticsearchHost, 1, false)
                .addLabeledComponent(new JBLabel("Elasticsearch port: "), elasticsearchPort, 1, false)
                .addComponent(noAuth, 1)
                .addComponent(new JLabel(""), 1)

                .addComponent(basicAuth, 1)
                .addLabeledComponent(usernameLabel, elasticsearchUsername, 1, false)
                .addLabeledComponent(userPasswordLabel, elasticsearchPassword, 1, false)
                .addComponent(anonymize, 1)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
        disableAuthentication(noAuth.isSelected());
    }

    private void disableAuthentication(boolean noAuthSelected) {
        elasticsearchUsername.setEnabled(noAuthSelected == false);
        elasticsearchPassword.setEnabled(noAuthSelected == false);
        usernameLabel.setEnabled(noAuthSelected == false);
        userPasswordLabel.setEnabled(noAuthSelected == false);
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

    public boolean getDetailedLogging() {
        return detailedLogging.isSelected();
    }

    public void setDetailedLogging(boolean newStatus) {
        detailedLogging.setSelected(newStatus);
    }

    public boolean getAnonymize() {
        return anonymize.isSelected();
    }

    public void setAnonymize(boolean newStatus) {
        anonymize.setSelected(newStatus);
    }

    public boolean getWithAuthentication() {
        return basicAuth.isSelected();
    }

    public void setWithAuthentication(boolean withAuth) {
        if(withAuth) {
            basicAuth.setSelected(true);
            noAuth.setSelected(false);
        } else {
            basicAuth.setSelected(false);
            noAuth.setSelected(true);
        }
    }
}