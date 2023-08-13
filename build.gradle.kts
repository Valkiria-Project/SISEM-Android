// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.daggerHilt) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.kotlinKapt) apply false
    alias(libs.plugins.kotlinParcelize) apply false
    id("org.jlleitschuh.gradle.ktlint") version "11.1.0" apply true
    id("io.gitlab.arturbosch.detekt") version "1.17.0" apply true
    id("com.github.ben-manes.versions") version "0.42.0" apply true
}

apply(from = "buildscripts/githooks.gradle")

subprojects {
    apply(from = "../buildscripts/ktlint.gradle")
    apply(from = "../buildscripts/detekt.gradle")
    apply(from = "../buildscripts/versionsplugin.gradle")
}

task("clean") {
    delete(rootProject.buildDir)
}

afterEvaluate {
    // We install the hook at the first occasion
    tasks["clean"].dependsOn("installGitHooks")
}
