plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.devtools.ksp") version "2.0.21-1.0.25"
}

android {
    namespace = "com.terrsus.terrorwear"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.terrsus.terrorwear"
        minSdk = 30
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    kotlinOptions {
        jvmTarget = "11"
    }

//    useLibrary("wear-sdk")

    buildFeatures {
        compose = true
    }
}

dependencies {
    // Wear OS
    implementation(libs.play.services.wearable)
    implementation(libs.wear.input)
    implementation(libs.wear.tooling.preview)

    // Compose BOM
    implementation(platform(libs.compose.bom))

    // Compose UI
    implementation(libs.ui)
    implementation(libs.ui.graphics)
    implementation(libs.ui.tooling.preview)

    // Wear Compose
    implementation(libs.compose.material)
    implementation(libs.compose.foundation)

    // Compose Runtime + Foundation
    implementation(libs.runtime)
    implementation(libs.foundation.layout)
    implementation(libs.foundation)

    // Misc
    implementation(libs.activity.compose)
    implementation(libs.core.splashscreen)
    implementation(libs.guava)

    implementation("androidx.datastore:datastore-preferences:1.1.1")

    implementation("androidx.room:room-runtime:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-cbor:1.6.3")

    implementation("androidx.security:security-crypto:1.1.0-alpha06")

    // Navigation
    implementation("androidx.navigation:navigation-compose:2.7.7")

    // Testing
    androidTestImplementation(platform(libs.compose.bom))
    androidTestImplementation(libs.ui.test.junit4)
    debugImplementation(libs.ui.tooling)
    debugImplementation(libs.ui.test.manifest)
}