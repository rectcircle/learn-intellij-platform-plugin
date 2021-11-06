package com.github.rectcircle.learnintellijplatformplugin.settings;


import com.intellij.openapi.options.Configurable;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * Controller，应用级别配置的实现，必须提供无参数构造函数。
 */
public class AppSettingsConfigurable implements Configurable {
    // 有一些标记接口，如 Configurable.NoScroll、Configurable.NoMargin，用来配置窗口的滚动和边框

    private AppSettingsComponent mySettingsComponent;

    @Nls(capitalization = Nls.Capitalization.Title)
    @Override
    public String getDisplayName() {
        return "SDK: Application Settings Example";
    }

    @Override
    public JComponent getPreferredFocusedComponent() {
        return mySettingsComponent.getPreferredFocusedComponent();
    }

    // 创建一个 Swing 组件，打开设置该设置窗口，该函数会被调用
    @Nullable
    @Override
    public JComponent createComponent() {
        mySettingsComponent = new AppSettingsComponent();
        return mySettingsComponent.getPanel();
    }

    // 用于判断是否 enable apply 按钮
    @Override
    public boolean isModified() {
        AppSettingsState settings = AppSettingsState.getInstance();
        boolean modified = !mySettingsComponent.getUserNameText().equals(settings.userId);
        modified |= mySettingsComponent.getIdeaUserStatus() != settings.ideaStatus;
        return modified;
    }

    // 点击 apply 触发
    @Override
    public void apply() {
        AppSettingsState settings = AppSettingsState.getInstance();
        settings.userId = mySettingsComponent.getUserNameText();
        settings.ideaStatus = mySettingsComponent.getIdeaUserStatus();
    }

    // 在 Configurable.createComponent 后立即被调用，在此处初始化 UI 值
    @Override
    public void reset() {
        AppSettingsState settings = AppSettingsState.getInstance();
        mySettingsComponent.setUserNameText(settings.userId);
        mySettingsComponent.setIdeaUserStatus(settings.ideaStatus);
    }

    // 用户点击 UI 上的确认或者取消，窗口销毁后会调用该函数
    @Override
    public void disposeUIResources() {
        mySettingsComponent = null;
    }

}