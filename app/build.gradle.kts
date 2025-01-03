plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp") version "1.9.0-1.0.13"
    kotlin("plugin.serialization") version "2.1.0"
}

android {
    namespace = "com.cs407.wellnest"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.cs407.wellnest"
        minSdk = 31
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.material3) // Using version catalog for Material3
    implementation(libs.navigation.compose) // Using version catalog for Navigation Compose
    implementation(libs.material)
    implementation(libs.material.calendar.view)
    implementation(libs.protolite.well.known.types)
    implementation(libs.androidx.storage)
    implementation(libs.androidx.ui.test.android)
    implementation(libs.androidx.room.ktx) // Using calendar view for calendar
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.common)
    implementation(libs.play.services.location)
    annotationProcessor(libs.androidx.room.compiler) // Only if you're using annotation processing in Java
    ksp(libs.androidx.room.compiler)                // For Kotlin Symbol Processing (recommended for Kotlin)
    implementation(libs.androidx.room.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)

    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation ("androidx.core:core-ktx:1.9.0")
    implementation ("androidx.compose.ui:ui:1.5.1" )// Replace with the latest version
    implementation ("androidx.compose.foundation:foundation:1.5.1")
    implementation ("androidx.compose.material3:material3:1.1.0") // For Material3 support
    implementation ("io.coil-kt:coil-compose:2.1.0")
    implementation ("pl.droidsonroids.gif:android-gif-drawable:1.2.25")
    implementation ("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.16.0")
    implementation("io.coil-kt:coil-compose:2.5.0")
    implementation("io.coil-kt:coil-gif:2.5.0")
    implementation ("com.google.android.gms:play-services-base:18.2.0")
    implementation ("androidx.activity:activity-compose:1.7.2")
    implementation ("androidx.lifecycle:lifecycle-runtime-compose:2.6.0")
    implementation ("androidx.navigation:navigation-compose:2.7.0")
    implementation ("androidx.media3:media3-exoplayer:1.0.0")
    implementation("com.google.android.gms:play-services-fitness:21.0.1")
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    implementation ("androidx.compose.material3:material3:1.3.1")

    implementation ("androidx.compose.material3:material3:1.2.0-alpha08")
    implementation ("androidx.compose.ui:ui:1.5.0")


    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1")
    implementation(libs.androidx.room.runtime)
    ksp(libs.androidx.room.compiler)
    implementation("io.github.sceneview:sceneview:2.2.1")


}