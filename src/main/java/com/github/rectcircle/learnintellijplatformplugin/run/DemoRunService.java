package com.github.rectcircle.learnintellijplatformplugin.run;

import com.github.rectcircle.learnintellijplatformplugin.run.configuration.DemoRunConfiguration;
import com.github.rectcircle.learnintellijplatformplugin.run.configuration.DemoRunConfigurationType;
import com.intellij.execution.ProgramRunnerUtil;
import com.intellij.execution.RunManager;
import com.intellij.execution.RunnerAndConfigurationSettings;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.openapi.project.Project;

public class DemoRunService {
    private final Project project;

    public DemoRunService(Project project) {
        this.project = project;
    }

    // 程序调起调试器
    public void run() {
        ProgramRunnerUtil.executeConfiguration(getSettings(), DefaultRunExecutor.getRunExecutorInstance());
    }

    private RunnerAndConfigurationSettings getSettings() {
        var runManager = RunManager.getInstance(project);
        var settingName = "Automatic generated";
        // var settings = runManager.findConfigurationByTypeAndName(new DemoRunConfigurationType(), settingName); // 可选的从已有配置中查找
        var settings = runManager.createConfiguration("Automatic generated", DemoRunConfigurationType.class);
        // runManager.addConfiguration(settings); // 可选的保存下来
        var config = (DemoRunConfiguration) settings.getConfiguration();
        config.setScriptName("test.sh");
        return settings;
    }

}
