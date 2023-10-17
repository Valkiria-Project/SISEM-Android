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
                password = "pk.eyJ1Ijoic2lzZW0iLCJhIjoiY2xuZGRrbDhqMDNhYzJ5bGkzZWpueTd4eCJ9.OgDIeo1x9xhvlT5NiAhKGg"
            }
        }
    }
}

rootProject.name = "SISEM Android"
include(":app")
include(":uicomponents")
