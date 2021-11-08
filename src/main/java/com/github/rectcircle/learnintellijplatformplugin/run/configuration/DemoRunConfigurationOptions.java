package com.github.rectcircle.learnintellijplatformplugin.run.configuration;

import com.intellij.execution.configurations.RunConfigurationOptions;
import com.intellij.openapi.components.StoredProperty;

// 运行配置项存储
public class DemoRunConfigurationOptions extends RunConfigurationOptions {

    private final StoredProperty<String> myScriptName = string("").provideDelegate(this, "scriptName");

    public String getScriptName() {
        return myScriptName.getValue(this);
    }

    public void setScriptName(String scriptName) {
        myScriptName.setValue(this, scriptName);
    }

}