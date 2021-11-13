package com.github.rectcircle.learnintellijplatformplugin.toolwindow;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentFactory;
import com.intellij.ui.jcef.*;
import com.jetbrains.cef.JCefAppConfig;
import org.cef.browser.CefBrowser;
import org.cef.browser.CefFrame;
import org.cef.handler.*;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class MyWebviewFactory implements ToolWindowFactory {

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        var panel = new JPanel();
        panel.setLayout(new BorderLayout());
        ContentFactory contentFactory = ContentFactory.SERVICE.getInstance();
        Content content = contentFactory.createContent(panel, "", false);
        toolWindow.getContentManager().addContent(content);

        // API 1 - JBCefApp 判断是否支持
        if (!JBCefApp.isSupported()) {
            var notSupportedLabel = new JLabel();
            notSupportedLabel.setText("Not support webview: see https://plugins.jetbrains.com/docs/intellij/jcef.html#jbcefapp");
            panel.add(notSupportedLabel);
            return;
        }
        // API 2 - 对 JBCefApp 进行配置的单例类，需在 JBCefApp.getInstance() 调用前进行配置（如 new JBCefBrowser()）。
        // 不建议进行配置，因为所有插件共享一个
        System.out.println(this.getClass().getClassLoader().toString());
        System.out.println(JBCefApp.class.getClassLoader().toString());
        JCefAppConfig.getInstance().getCefSettings();
        // API 3 - JBCefBrowser Jetbrains 对 Cef 的封装，包含 JBCefClient 和 CefBrowser
        // 给定 URL 或者 HTML 创建一个浏览器实例
        // API 3.1 - 指定 URL 从网络上加载
//        var jbCefBrowser = new JBCefBrowser("https://rectcircle.cn");
        var jbCefBrowser =  new JBCefBrowser();

        // API 3.2 - 指定 HTML 直接加载（打开开发者工具，刷新后就没了。看实现是，读取一次后就删掉了 JBCefFileSchemeHandlerFactory）
        jbCefBrowser.loadHTML(
                "<!DOCTYPE html><html lang=\"en\"><head><title>Test</title></head><body>拼成的HTML，不是从 URL 加载的</body></html>",
                "https://rectcircle.cn"  // 可选的，最终浏览器访问的是 file:///jbcefbrowser/随机数#url=https://rectcircle.cn 走的文件协议，应该还是有跨域问题
        );
        jbCefBrowser.getJBCefClient().addLoadHandler(new CefLoadHandlerAdapter() { // 解决刷新问题的方案（后果是浏览器上会产生无效的 history），更好的做法是不使用 JBCefApp 来创建 JBCefBrowser，而是使用自建的 CefApp 来创建 CefClient 和 CefBrowser
            @Override
            public void onLoadError(CefBrowser browser, CefFrame frame, ErrorCode errorCode, String errorText, String failedUrl) {
                if (errorCode == ErrorCode.ERR_FILE_NOT_FOUND && failedUrl.startsWith("file:///jbcefbrowser")) {
                    jbCefBrowser.loadHTML(
                            "<!DOCTYPE html><html lang=\"en\"><head><title>Test</title></head><body>拼成的HTML，不是从 URL 加载的 2</body></html>",
                            "https://rectcircle.cn"  // 可选的，最终浏览器访问的是 file:///jbcefbrowser/随机数#url=https://rectcircle.cn 走的文件协议，应该还是有跨域问题
                    );
                }
            }
        }, jbCefBrowser.getCefBrowser());
        panel.add(jbCefBrowser.getComponent(), BorderLayout.CENTER);
        // API 4 - JBCefClient 可以添加一些事件回调，拦截网络请求等
        // API 5 - CefBrowser 动态执行 JS 代码，获取 Dom 等
        jbCefBrowser.getJBCefClient().addLoadHandler(new CefLoadHandlerAdapter() {
            @Override
            public void onLoadEnd(CefBrowser browser, CefFrame frame, int httpStatusCode) {
                // API 5.1 - 动态执行 JS 代码
                browser.executeJavaScript(
                        "setInterval(()=>{console.log(\"Java 调用的\")}, 1000)"
                        , "https://rectcircle.cn/js/main.js" // 假装这个代码是从该 URL 中下载的
                        , 0);
            }
        }, jbCefBrowser.getCefBrowser());
        // API 6 - JBCefJSQuery JS 调用 Java 回调函数
        final JBCefJSQuery myJSQuery = JBCefJSQuery.create((JBCefBrowserBase) jbCefBrowser);
        myJSQuery.addHandler((args) -> {
            System.out.println("JS 调用了 这个函数，参数是：" + args);
            if ("null".equals(args)) {
                return new JBCefJSQuery.Response(null, 1, "不允许 null");
            } else if ("undefined".equals(args)) {
                return new JBCefJSQuery.Response(null); // 这样 JS 侧，会掉 onFailure
            }
            return new JBCefJSQuery.Response("Java 的返回值");
        });
        jbCefBrowser.getJBCefClient().addLoadHandler(new CefLoadHandlerAdapter() {
            @Override
            public void onLoadEnd(CefBrowser browser, CefFrame frame, int httpStatusCode) {
                // 将模块注入到浏览器中执行里面
                /*
                window.JavaPanelBridge = {
                    callJava: function(arg) {
                        window.cefQuery_762768232_1({
                            request: '' + JSON.stringify(arg),
                            onSuccess: response=>console.log('callJava 成功', response),
                            onFailure: (error_code,error_message)=>console.log('callJava 失败', error_code, error_message)
                        });
                    }
                };
                */
                browser.executeJavaScript(
                        "window.JavaPanelBridge = {" +
                                "callJava : function(arg) {" +
                                myJSQuery.inject(
                                        "JSON.stringify(arg)",
                                        "response => console.log('callJava 成功', response)",
                                        "(error_code, error_message) => console.log('callJava 失败', error_code, error_message)"
                                    ) +
                                "}" +
                            "};" +
                            "setInterval(()=>{JavaPanelBridge.callJava(); JavaPanelBridge.callJava(null); JavaPanelBridge.callJava({a:1}); JavaPanelBridge.callJava(\"这是参数\");}, 5000)",
                        "https://rectcircle.cn/js/js-bridge.js", 0);
            }
        }, jbCefBrowser.getCefBrowser());

    }
}
