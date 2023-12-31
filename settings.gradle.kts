pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")

        maven {
            url = uri("https://api.mapbox.com/downloads/v2/releases/maven")
            authentication {
                create<BasicAuthentication>("basic")
            }
            credentials {
                username = "mapbox"
                password = "sk.eyJ1Ijoic2lzZW0iLCJhIjoiY2xwa2l5bmxmMDB0NDJrankxeG4yZWowMSJ9.o6RJNwx7MDzeUDturbT1LA"
            }
        }
    }
}

rootProject.name = "SISEM Android"
include(":app")
include(":uicomponents")
