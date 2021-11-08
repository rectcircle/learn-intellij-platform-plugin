package com.github.rectcircle.learnintellijplatformplugin.run.execution;

import com.github.rectcircle.learnintellijplatformplugin.run.configuration.DemoRunConfiguration;
import com.goide.dlv.DlvDebugProcess;
import com.goide.dlv.DlvDisconnectOption;
import com.goide.dlv.DlvRemoteVmConnection;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.ExecutionManager;
import com.intellij.execution.ExecutionResult;
import com.intellij.execution.configurations.RunProfile;
import com.intellij.execution.configurations.RunnerSettings;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.runners.ProgramRunner;
import com.intellij.execution.runners.RunContentBuilder;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.openapi.fileEditor.FileDocumentManager;
import com.intellij.xdebugger.XDebugProcess;
import com.intellij.xdebugger.XDebugProcessStarter;
import com.intellij.xdebugger.XDebugSession;
import com.intellij.xdebugger.XDebuggerManager;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import java.net.InetSocketAddress;

// 一个 Runner：实现运行配置启动后的的行为
public class DemoProgramRunner implements ProgramRunner<RunnerSettings>{
    @Override
    public @NotNull @NonNls String getRunnerId() {
        return "DemoProgramRunner";
    }

    private DlvDebugProcess process;

    // 该此执行是否有该 Runner 负责，同时处理 Debug 和 Run 两种场景
    @Override
    public boolean canRun(@NotNull String executorId, @NotNull RunProfile profile) {
        return (ExecutionUtil.isDebugMode(executorId) || ExecutionUtil.isRunMode(executorId))
                && profile instanceof DemoRunConfiguration;
    }

    @Override
    public void execute(@NotNull ExecutionEnvironment environment) throws ExecutionException {
        // 启动 run 配置，调用 `RunProfile` 的 getState 方法
        ExecutionManager.getInstance(environment.getProject()).startRunProfile(environment, state -> {
            // 先保存所有未保存文件
            FileDocumentManager.getInstance().saveAllDocuments();
            // state 为 DemoRunConfiguration.getState() 返回，即 DemoRunProfileState，调用 RunProfileState.execute
            ExecutionResult executionResult = state.execute(environment.getExecutor(), this);
            if (executionResult == null) {
                return null;
            }
            // 获取到 RunContentDescriptor
            if (ExecutionUtil.isDebugMode(environment)) {
                return this.debugModeRunContentDescriptor(environment, executionResult);
            } else if (ExecutionUtil.isRunMode(environment)) {
                return this.runModeRunContentDescriptor(environment, executionResult);
            }
            throw new ExecutionException("Not support");
        });
    }

    // 给 RunProfileState 用，控制连接到 dlv（因为进程启动了，不一定就可以立即连接了，需要 State 自己决定合适连接）
    public void connectToDlv(String host, int port) {
        process.connect(new InetSocketAddress(host, port));
    }

    private RunContentDescriptor runModeRunContentDescriptor(@NotNull ExecutionEnvironment environment, @NotNull ExecutionResult executionResult) throws ExecutionException {
        // 简单返回一个 RunContentDescriptor
        return new RunContentBuilder(executionResult, environment).showRunContent(environment.getContentToReuse());
    }

    private RunContentDescriptor debugModeRunContentDescriptor(@NotNull ExecutionEnvironment environment, @NotNull ExecutionResult executionResult) throws ExecutionException {
        // Debug 调试器附加到进程中，然后一个 Bugger RunContentDescriptor
        return XDebuggerManager.getInstance(environment.getProject()).startSession(environment, new XDebugProcessStarter() {
            @Override
            @NotNull
            public XDebugProcess start(@NotNull XDebugSession session) throws ExecutionException {
                process = new DlvDebugProcess(
                        session,
                        new DlvRemoteVmConnection(DlvDisconnectOption.DETACH),
                        executionResult,
                        true);
                return process;
            }
        }).getRunContentDescriptor();
    }
}
