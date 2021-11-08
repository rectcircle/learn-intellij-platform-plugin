package com.github.rectcircle.learnintellijplatformplugin.run.configuration;


import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.ui.LabeledComponent;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

// 运行配置项编辑器 UI：一种特殊的 UI，用来编辑展示运行配置项
public class DemoSettingsEditor extends SettingsEditor<DemoRunConfiguration> {

    private JPanel myPanel;
    private LabeledComponent<TextFieldWithBrowseButton> myScriptName;

    @Override
    protected void resetEditorFrom(DemoRunConfiguration demoRunConfiguration) {
        myScriptName.getComponent().setText(demoRunConfiguration.getScriptName());
    }

    @Override
    protected void applyEditorTo(@NotNull DemoRunConfiguration demoRunConfiguration) {
        demoRunConfiguration.setScriptName(myScriptName.getComponent().getText());
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        return myPanel;
    }

    private void createUIComponents() {
        myScriptName = new LabeledComponent<>();
        myScriptName.setComponent(new TextFieldWithBrowseButton());
    }

}