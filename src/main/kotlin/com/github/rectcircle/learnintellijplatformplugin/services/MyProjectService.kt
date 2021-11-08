package com.github.rectcircle.learnintellijplatformplugin.services

import com.intellij.openapi.project.Project
import com.github.rectcircle.learnintellijplatformplugin.MyBundle
import com.intellij.ide.plugins.PluginManager
import com.intellij.ide.plugins.PluginManagerCore

class MyProjectService(project: Project) {

    init {
        // 获取到插件路径，即可读取到外部资源路径
        println(PluginManagerCore.getPlugin(PluginManager.getPluginByClassName(this.javaClass.name))?.pluginPath)
        println(MyBundle.message("projectService", project.name))
    }
}
