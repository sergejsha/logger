pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "logger"
includeBuild("build-plugins")
include(":logger")
include(":sample-apps:shared")
include(":sample-apps:composeApp")
