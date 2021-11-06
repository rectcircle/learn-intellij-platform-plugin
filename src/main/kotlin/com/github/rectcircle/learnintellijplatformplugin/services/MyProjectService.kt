package com.github.rectcircle.learnintellijplatformplugin.services

import com.intellij.openapi.project.Project
import com.github.rectcircle.learnintellijplatformplugin.MyBundle

class MyProjectService(project: Project) {

    init {
        println(MyBundle.message("projectService", project.name))
    }
}
