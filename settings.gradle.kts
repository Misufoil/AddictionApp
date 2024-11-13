pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }

}


dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "AddictionApp"
include(":app")
include(":database")
include(":features:addictions-home")
include(":addictions-data")
include(":core-utils")
include(":addictions-uikit")
include(":features:addictions-add-edit")
include(":features:addictions-details")
include(":core")
include(":navigation")
include(":activity:activity-impl")
include(":activity:activity-api")
