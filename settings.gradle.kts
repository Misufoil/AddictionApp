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
include(":features:addictions-home-impl")
include(":addictions-data")
include(":core-utils")
include(":addictions-uikit")
include(":features:addictions-edit-impl")
include(":features:addictions-details-impl")
include(":features:addictions-details-api")
include(":features:addictions-home-api")
include(":features:addictions-edit-api")
include(":core")
include(":core:feature-api")
