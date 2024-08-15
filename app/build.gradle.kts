plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.daggerHilt)
    alias(libs.plugins.detekt)
    alias(libs.plugins.googleServices)
    alias(libs.plugins.kotlinKsp)
    alias(libs.plugins.kotlinParcelize)
    alias(libs.plugins.ktlint)
}

android {
    namespace = "com.skgtecnologia.sisem"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.skgtecnologia.sisem"
        minSdk = 30
        targetSdk = 34
        versionCode = 65
        versionName = "2.0.0"
        setProperty("archivesBaseName", "$applicationId-v$versionName($versionCode)")

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    buildTypes {
        debug {
            applicationIdSuffix = ".debug"
            isDebuggable = true
            buildConfigField("String", "AUTH_BASE_URL", "\"https://test.emergencias-sisem.co/dev/sisem-api/\"")
            buildConfigField("String", "BASE_URL", "\"https://test.emergencias-sisem.co/dev/sisem-api/v1/\"")
            buildConfigField("String", "LOCATION_URL", "\"https://test.emergencias-sisem.co/dev/sisem-location-api/v1/\"")
            buildConfigField("String", "REFRESH_URL", "\"https://admin.qa.sisembogota.com/auth/realms/sisem/protocol/openid-connect/token\"")
        }
        create("staging") {
            initWith(getByName("debug"))
            applicationIdSuffix = ".debugStaging"
            buildConfigField("String", "AUTH_BASE_URL", "\"https://test.emergencias-sisem.co/qa/sisem-api/\"")
            buildConfigField("String", "BASE_URL", "\"https://test.emergencias-sisem.co/qa/sisem-api/v1/\"")
            buildConfigField("String", "LOCATION_URL", "\"https://test.emergencias-sisem.co/qa/sisem-location-api/v1/\"")
            buildConfigField("String", "REFRESH_URL", "\"https://admin.qa.sisembogota.com/auth/realms/sisem/protocol/openid-connect/token\"")
        }
        create("preProd") {
            initWith(getByName("debug"))
            isDebuggable = true
            applicationIdSuffix = ".preProd"
            buildConfigField("String", "AUTH_BASE_URL", "\"https://mobile-preprod.sisem.co/sisem-api/\"")
            buildConfigField("String", "BASE_URL", "\"https://mobile-preprod.sisem.co/sisem-api/v1/\"")
            buildConfigField("String", "LOCATION_URL", "\"https://mobile-preprod.sisem.co/sisem-location-api/v1/\"")
            buildConfigField("String", "REFRESH_URL", "\"https://admin-preprod.sisem.co/auth/realms/sisem/protocol/openid-connect/token\"")
        }
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
            buildConfigField("String", "AUTH_BASE_URL", "\"http://34.74.218.181/sisem-api/\"")
            buildConfigField("String", "BASE_URL", "\"http://34.74.218.181/sisem-api/v1/\"")
            buildConfigField("String", "LOCATION_URL", "\"http://34.74.218.181/sisem-location-api/v1/\"")
            buildConfigField("String", "REFRESH_URL", "\"https://admin.prod.sisembogota.com/auth/realms/sisem/protocol/openid-connect/token\"")
        }
    }
    buildFeatures {
        buildConfig = true
        compose = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs +=
            listOf(
                "-opt-in=androidx.compose.foundation.ExperimentalFoundationApi",
                "-opt-in=androidx.compose.foundation.layout.ExperimentalLayoutApi",
                "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
                "-opt-in=androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi",
                "-opt-in=androidx.compose.ui.ExperimentalComposeUiApi",
                "-opt-in=com.google.accompanist.permissions.ExperimentalPermissionsApi",
                "-opt-in=kotlin.contracts.ExperimentalContracts",
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
                "-P",
                "plugin:androidx.compose.compiler.plugins.kotlin:experimentalStrongSkipping=true",
            )
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

composeCompiler {
    enableStrongSkippingMode = true

    reportsDestination = layout.buildDirectory.dir("compose_compiler")
}

dependencies {

    implementation(project(":uicomponents"))

    // Android
    implementation(libs.core.ktx)
    implementation(libs.androidx.core.splashscreen)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.activity.compose)
    implementation(libs.androidx.constraintlayout.compose)

    // Compose
    implementation(platform(libs.compose.bom))
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.text.google.fonts)
    implementation(libs.ui.tooling.preview)
    implementation(libs.material3)
    implementation(libs.material3.window.size)
    implementation(libs.material.icons.extended)

    // Compose performance
    implementation(libs.kotlinx.collections.immutable)

    // Desugaring
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    // Firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messaging)

    // Logging
    implementation(libs.timber)

    // Dependency Injection
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.androidx.hilt.navigation.compose)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // Networking
    implementation(platform(libs.okhttp.bom))
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)

    implementation(libs.retrofit)
    implementation(libs.converter.moshi)

    // Deserializer
    implementation(libs.moshi)
    implementation(libs.moshi.kotlin)
    ksp(libs.moshi.codegen)
    implementation(libs.moshi.adapters)

    // Local Storage
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    // Media
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.extensions)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
    implementation(libs.accompanist.permissions)
    implementation(libs.accompanist.placeholder.material)
    implementation(libs.coil.compose)

    // Location
    implementation(libs.play.services.location)

    // Unit Testing
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.androidx.core.testing)
    testImplementation(libs.kotlinx.coroutines.test)

    // UI Testing
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)

    // Debug
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
    debugImplementation(libs.leakcanary.android)
}
