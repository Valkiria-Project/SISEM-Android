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
                password = "sk.eyJ1IjoianZpbGxhZDEiLCJhIjoiY2xra3NrMHltMDZjaTNpcG1ya3hwamgzeiJ9.q1dD6a0zNNyB_9765lu-Tw"
            }
        }
    }
}

rootProject.name = "SISEM Android"
include(":app")
include(":uicomponents")
