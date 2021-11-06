package com.github.rectcircle.learnintellijplatformplugin.settings;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@State(
        name = "org.intellij.sdk.settings.AppSettingsState", // 该状态存储的唯一标识符，最终会作为 xml 文件的 tags
        // reloadable = false, // default true，如果序列化的文件被外部程序更改了，是否重新加载窗口
        storages = @Storage("SdkSettingsPlugin.xml") // 存储文件名，Project 级别状态，且不需要存储到代码仓库，需使用 StoragePathMacros.WORKSPACE_FILE
)
public class AppSettingsState implements PersistentStateComponent<AppSettingsState> {

    public String userId = "John Q. Public";
    public boolean ideaStatus = false;

    public static AppSettingsState getInstance() {
        return ApplicationManager.getApplication().getService(AppSettingsState.class);
    }

    @Nullable
    @Override
    public AppSettingsState getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull AppSettingsState state) {
        XmlSerializerUtil.copyBean(state, this);
    }

}