package com.github.rectcircle.learnintellijplatformplugin.run.execution;

import com.github.rectcircle.learnintellijplatformplugin.run.configuration.DemoRunConfiguration;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.*;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.execution.ui.RunContentManager;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.util.Key;
import com.intellij.xdebugger.XDebuggerManager;
import org.jetbrains.annotations.NotNull;

public class DemoRunProfileState extends CommandLineState {

    public DemoRunProfileState(ExecutionEnvironment environment) {
        super(environment);
    }

    @Override
    protected @NotNull ProcessHandler startProcess() throws ExecutionException {
        var environment = this.getEnvironment();
        var demoRunConfiguration = (DemoRunConfiguration)  environment.getRunnerAndConfigurationSettings().getConfiguration();

        Messages.showInfoMessage(demoRunConfiguration.getScriptName(), "这是用户的配置");

        // 下面是创建一个外部命令行进程
        GeneralCommandLine commandLine;
        if (ExecutionUtil.isRunMode(environment)) { // Run 模式
            commandLine = new GeneralCommandLine("go", "run", "./");
        } else if (ExecutionUtil.isDebugMode(environment)) { // Debug 模式
            commandLine = new GeneralCommandLine("dlv debug --headless --listen=:2345 --api-version=2 --accept-multiclient ./".split(" "));
        } else {
            throw new ExecutionException("Not support");
        }
        commandLine.setWorkDirectory(environment.getProject().getBasePath());
        OSProcessHandler processHandler = ProcessHandlerFactory.getInstance().createColoredProcessHandler(commandLine);
        ProcessTerminatedListener.attach(processHandler);

        // 添加进程的事件监听
        processHandler.addProcessListener(new ProcessAdapter() {
            private boolean connected = false;

            // 进程终止的回调
            @Override
            public void processTerminated(@NotNull ProcessEvent event) {
                getConsoleView(processHandler).print("进程结束停止了", ConsoleViewContentType.SYSTEM_OUTPUT);
            }

            // 检测到进程有输出时的回调
            @Override
            public void onTextAvailable(@NotNull ProcessEvent event, @NotNull Key outputType) {
                // 检测到关键词后，连接到 dlv
                if (!connected && ExecutionUtil.isDebugMode(environment) && event.getText().contains("API server listening at")) {
                    getConsoleView(processHandler).print("即将连接到 dlv", ConsoleViewContentType.SYSTEM_OUTPUT);
                    ((DemoProgramRunner) environment.getRunner()).connectToDlv("localhost", 2345);
                    connected = true;
                }
            }

        });
        return processHandler;
    }

    // 获取到执行页面的 Console，可以打印一些自定义的内容
    private ConsoleView getConsoleView(ProcessHandler processHandler) {
        var environment = this.getEnvironment();
        var project = environment.getProject();
        if (ExecutionUtil.isDebugMode(environment)) {
            var session = XDebuggerManager.getInstance(project).getCurrentSession();
            if (session != null) {
                return session.getConsoleView();
            }
        }
        RunContentDescriptor contentDescriptor = RunContentManager
                .getInstance(project)
                .findContentDescriptor(environment.getExecutor(), processHandler);
        ConsoleView console = null;
        if (contentDescriptor != null && contentDescriptor.getExecutionConsole() instanceof ConsoleView) {
            console = (ConsoleView) contentDescriptor.getExecutionConsole();
        }
        return console;
    }
}
