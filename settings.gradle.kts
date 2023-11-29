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
                password = "sk.eyJ1IjoibGlsaTI5IiwiYSI6ImNscGplMGJrdDA3dXAyanA2ZDA0MjI0ZGIifQ.7IhKdgvej1r20MBmBXlxRg"
            }
        }
    }
}

rootProject.name = "SISEM Android"
include(":app")
include(":uicomponents")
