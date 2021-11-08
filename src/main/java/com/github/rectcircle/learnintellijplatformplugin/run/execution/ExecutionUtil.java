package com.github.rectcircle.learnintellijplatformplugin.run.execution;

import com.intellij.execution.executors.DefaultDebugExecutor;
import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.runners.ExecutionEnvironment;

public class ExecutionUtil {

    public static boolean isDebugMode(String executorId) {
        return DefaultDebugExecutor.EXECUTOR_ID.equals(executorId);
    }

    public static boolean isDebugMode(ExecutionEnvironment environment) {
        return isDebugMode(environment.getExecutor().getId());
    }

    public static boolean isRunMode(String executorId) {
        return DefaultRunExecutor.EXECUTOR_ID.equals(executorId);
    }

    public static boolean isRunMode(ExecutionEnvironment environment) {
        return isRunMode(environment.getExecutor().getId());
    }
}
