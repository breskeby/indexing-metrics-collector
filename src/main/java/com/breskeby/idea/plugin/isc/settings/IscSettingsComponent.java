package com.breskeby.idea.plugin.isc.settings;

import com.intellij.ui.components.JBCheckBox;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPasswordField;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import org.jetbrains.annotations.NotNull;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * Supports creating and managing a {@link JPanel} for the Settings Dialog.
 */
public class IscSettingsComponent {

    private final JPanel myMainPanel;
    private final JBTextField elasticsearchHost = new JBTextField();
    private final JBTextField elasticsearchPort = new JBTextField();
    private final JBTextField elasticsearchUsername = new JBTextField();
    private final JBPasswordField elasticsearchPassword = new JBPasswordField();

    private final JBCheckBox detailedLogging = new JBCheckBox("Detailed logging");
    private final JBCheckBox anonymize = new JBCheckBox("Anonymize user data");

    public IscSettingsComponent() {
//        JBComponent jbComponent = JBComponent();
        myMainPanel = FormBuilder.createFormBuilder()

                .addLabeledComponent(new JBLabel("Elasticsearch host: "), elasticsearchHost, 1, false)
                .addLabeledComponent(new JBLabel("Elasticsearch port: "), elasticsearchPort, 1, false)
                .addLabeledComponent(new JBLabel("Elasticsearch username: "), elasticsearchUsername, 1, false)
                .addLabeledComponent(new JBLabel("Elasticsearch password: "), elasticsearchPassword, 1, false)
//                .addComponent(detailedLogging, 1)
                .addComponent(anonymize, 1)
                .addComponentFillVertically(new JPanel(), 0)
                .getPanel();
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

}