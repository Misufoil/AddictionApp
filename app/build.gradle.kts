plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.dagger.hilt.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kapt)
}

android {
    namespace = "dev.misufoil.addictionapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "dev.misufoil.addictionapp"
        minSdk = 28
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

    implementation(libs.dagger.hilt.android)
    implementation(libs.androidx.hilt.common)
    implementation(libs.androidx.hilt.work)
    kapt(libs.dagger.hilt.compiler)
    kapt(libs.androidx.hilt.compiler)

    implementation(libs.androidx.navigation.compose)

    implementation(libs.androidx.work)

    implementation(project(":navigation"))
    implementation(project(":activity:activity-api"))
    implementation(project(":activity:activity-impl"))
    implementation(project(":features:addictions-home"))
    implementation(project(":features:addictions-add-edit"))
    implementation(project(":features:addictions-details"))
    implementation(project(":database"))
    implementation(project(":core-utils"))
    implementation(project(":addictions-uikit"))

}