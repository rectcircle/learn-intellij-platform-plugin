package com.github.rectcircle.learnintellijplatformplugin.toolwindow;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import org.jetbrains.annotations.NotNull;

public class MyToolWindowFactory implements ToolWindowFactory {

    /**
     * 创建一个 ToolWindows 窗口
     *
     * @param project    当前视图
     * @param toolWindow 当前 tool window
     */
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        MyToolWindow myToolWindow = new MyToolWindow(project, toolWindow);
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(myToolWindow.getContent(), "", false);
        toolWindow.getContentManager().addContent(content);
    }

}