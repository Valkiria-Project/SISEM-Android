// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.benManesversions) apply true
    alias(libs.plugins.daggerHilt) apply false
    alias(libs.plugins.detekt) apply true
    alias(libs.plugins.googleServices) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.kotlinKsp) apply false
    alias(libs.plugins.kotlinParcelize) apply false
    alias(libs.plugins.ktlint) apply true
}

apply(from = "buildscripts/githooks.gradle")

subprojects {
    apply(from = "../buildscripts/ktlint.gradle")
    apply(from = "../buildscripts/detekt.gradle")
    apply(from = "../buildscripts/versionsplugin.gradle")

    // Compose metric configuration
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            if (project.findProperty("composeCompilerReports") == "true") {
                freeCompilerArgs += listOf(
                    "-P",
                    "plugin:androidx.compose.compiler.plugins.kotlin:reportsDestination=${project.buildDir.absolutePath}/compose_compiler"
                )
            }
            if (project.findProperty("composeCompilerMetrics") == "true") {
                freeCompilerArgs += listOf(
                    "-P",
                    "plugin:androidx.compose.compiler.plugins.kotlin:metricsDestination=${project.buildDir.absolutePath}/compose_compiler"
                )
            }
        }
    }
}

task("clean") {
    delete(rootProject.layout.buildDirectory)
}

afterEvaluate {
    // We install the hook at the first occasion
    tasks["clean"].dependsOn("installGitHooks")
}
