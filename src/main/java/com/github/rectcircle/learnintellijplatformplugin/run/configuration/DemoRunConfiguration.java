package com.github.rectcircle.learnintellijplatformplugin.run.configuration;


import com.github.rectcircle.learnintellijplatformplugin.run.execution.DemoRunProfileState;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.*;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

// 运行配置：表示一个运行调试配置实例，会和一个运行配置项编辑器 UI、以及一个运行配置状态存储绑定
public class DemoRunConfiguration extends RunConfigurationBase<DemoRunConfigurationOptions> {

    protected DemoRunConfiguration(Project project, ConfigurationFactory factory, String name) {
        super(project, factory, name);
    }

    @NotNull
    @Override
    protected DemoRunConfigurationOptions getOptions() {
        return (DemoRunConfigurationOptions) super.getOptions();
    }

    public String getScriptName() {
        return getOptions().getScriptName();
    }

    public void setScriptName(String scriptName) {
        getOptions().setScriptName(scriptName);
    }

    @NotNull
    @Override
    public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        return new DemoSettingsEditor();
    }

    @Override
    public void checkConfiguration() {
    }

    // 核心入口，获取到 RunProfileState 实现
    @Nullable
    @Override
    public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment executionEnvironment) {
//        // 先简单的实现为直接通过命令行执行
//        return new CommandLineState(executionEnvironment) {
//            @NotNull
//            @Override
//            protected ProcessHandler startProcess() throws ExecutionException {
//                GeneralCommandLine commandLine = new GeneralCommandLine(getOptions().getScriptName());
//                OSProcessHandler processHandler = ProcessHandlerFactory.getInstance().createColoredProcessHandler(commandLine);
//                ProcessTerminatedListener.attach(processHandler);
//                return processHandler;
//            }
//        };
        return new DemoRunProfileState(executionEnvironment);
    }
}
