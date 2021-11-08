package com.github.rectcircle.learnintellijplatformplugin.run.configuration;


import com.intellij.execution.configurations.ConfigurationFactory;
import com.intellij.execution.configurations.ConfigurationType;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.openapi.components.BaseState;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// 运行配置工厂：对应 IDE Run Configuration 的模板列表页中每一个子项，负责创建 `RunConfiguration`
public class DemoConfigurationFactory extends ConfigurationFactory {

    // ConfigurationType 作为工厂的成员
    protected DemoConfigurationFactory(ConfigurationType type) {
        super(type);
    }

    @Override
    public @NotNull String getId() {
        return DemoRunConfigurationType.ID;
    }

    // 获取到一个模板配置
    @NotNull
    @Override
    public RunConfiguration createTemplateConfiguration(@NotNull Project project) {
        return new DemoRunConfiguration(project, this, "Demo");
    }

    // 声明该配置的选项声明类是什么
    @Nullable
    @Override
    public Class<? extends BaseState> getOptionsClass() {
        return DemoRunConfigurationOptions.class;
    }

}