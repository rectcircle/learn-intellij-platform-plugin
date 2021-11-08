package com.github.rectcircle.learnintellijplatformplugin.run.configuration;


import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.icons.AllIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

// 运行配置类型：对应 IDE Run Configuration 的模板列表页中每一个顶级项目，负责声明关联的 `ConfigurationFactory` 并作为唯一标识符
public class DemoRunConfigurationType implements ConfigurationType {

    // 配置类型 ID
    public static final String ID = "DemoRunConfiguration";

    // 展示名
    @NotNull
    @Override
    public String getDisplayName() {
        return "Demo";
    }

    // 描述
    @Override
    public String getConfigurationTypeDescription() {
        return "Demo run configuration type";
    }

    @Override
    public Icon getIcon() {
        return AllIcons.General.Information;
    }

    @NotNull
    @Override
    public String getId() {
        return ID;
    }

    // 配置工厂列表，每一个配置工厂就是一种运行配置（模板）
    @Override
    public ConfigurationFactory[] getConfigurationFactories() {
        return new ConfigurationFactory[]{new DemoConfigurationFactory(this)};
    }

}