package com.github.rectcircle.learnintellijplatformplugin.services

import com.intellij.openapi.project.Project
import com.github.rectcircle.learnintellijplatformplugin.MyBundle

class MyProjectService(project: Project) {

    init {
        // jar:file:/Users/.../learn-intellij-platform-plugin/build/idea-sandbox/plugins/learn-intellij-platform-plugin/lib/learn-intellij-platform-plugin-0.0.1.jar!/com/github/rectcircle/learnintellijplatformplugin/services/MyProjectService.class
        // 通过获取代码所在 jar 包即可获取到插件路径，即可读取到外部资源
        println(this.javaClass.protectionDomain.codeSource.location)
        println(MyBundle.message("projectService", project.name))
    }
}
