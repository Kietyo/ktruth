pluginManagement {
    repositories {  mavenLocal(); mavenCentral(); google(); gradlePluginPortal()  }
    val kotlinVersion: String by settings
    plugins {
        kotlin("multiplatform") version kotlinVersion
    }
}

plugins {
    id("com.soywiz.kproject.settings") version "0.3.1"
}


rootProject.name = "ktruth"

//kproject("./deps")