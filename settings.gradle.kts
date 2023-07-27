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
                username = providers.gradleProperty("MAPBOX_USER").get()
                password = providers.gradleProperty("MAPBOX_TOKEN").get()
            }
        }
    }
}

rootProject.name = "SISEM Android"
include(":app")
include(":uicomponents")
