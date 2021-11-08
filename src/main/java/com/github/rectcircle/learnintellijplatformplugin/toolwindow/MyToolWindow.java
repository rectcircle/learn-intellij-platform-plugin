package com.github.rectcircle.learnintellijplatformplugin.toolwindow;

import com.github.rectcircle.learnintellijplatformplugin.run.DemoRunService;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;

import javax.swing.*;
import java.util.Calendar;

public class MyToolWindow {

    private JButton refreshToolWindowButton;
    private JButton hideToolWindowButton;
    private JLabel currentDate;
    private JLabel currentTime;
    private JLabel timeZone;
    private JPanel myToolWindowContent;
    private JButton runButton;
    private final Project project;

    // 构造函数添加一个 project 属性，同步修改 src/main/java/com/github/rectcircle/learnintellijplatformplugin/toolwindow/MyToolWindowFactory.java
    public MyToolWindow(Project project, ToolWindow toolWindow) {
        this.project = project;
        // 添加两个按钮的回调函数
        hideToolWindowButton.addActionListener(e -> toolWindow.hide(null));
        refreshToolWindowButton.addActionListener(e -> currentDateTime());
        runButton.addActionListener(e -> {
            var demoRunService = project.getService(DemoRunService.class);
            if (demoRunService != null) {
                demoRunService.run();
            }
        });
        this.currentDateTime();
    }

    // 刷新 UI 组件状态
    public void currentDateTime() {
        // Get current date and time
        Calendar instance = Calendar.getInstance();
        currentDate.setText(
                instance.get(Calendar.DAY_OF_MONTH) + "/"
                        + (instance.get(Calendar.MONTH) + 1) + "/"
                        + instance.get(Calendar.YEAR)
        );
        currentDate.setIcon(new ImageIcon(getClass().getResource("/toolWindow/Calendar-icon.png")));
        int min = instance.get(Calendar.MINUTE);
        String strMin = min < 10 ? "0" + min : String.valueOf(min);
        currentTime.setText(instance.get(Calendar.HOUR_OF_DAY) + ":" + strMin);
        currentTime.setIcon(new ImageIcon(getClass().getResource("/toolWindow/Time-icon.png")));
        // Get time zone
        long gmt_Offset = instance.get(Calendar.ZONE_OFFSET); // offset from GMT in milliseconds
        String str_gmt_Offset = String.valueOf(gmt_Offset / 3600000);
        str_gmt_Offset = (gmt_Offset > 0) ? "GMT + " + str_gmt_Offset : "GMT - " + str_gmt_Offset;
        timeZone.setText(str_gmt_Offset);
        timeZone.setIcon(new ImageIcon(getClass().getResource("/toolWindow/Time-zone-icon.png")));
    }

    public JPanel getContent() {
        // 只有 Goland 场景才激活该按钮
        if (project.getService(DemoRunService.class) == null) {
            runButton.setEnabled(false);
        }
        return myToolWindowContent;
    }

}