 plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
     alias(libs.plugins.compose.compiler)
}

android {
    namespace = "dev.misufoil.core_utils"
    compileSdk = 34

    defaultConfig {
        minSdk = 28

        consumerProguardFiles("consumer-rules.pro")
    }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.courutines.core)
    implementation(libs.dagger.hilt.android)


    implementation(platform(libs.androidx.compose.bom))

    implementation(project(":addictions-uikit"))
}