plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.lianyi.wubbaboo"
    compileSdk = rootProject.ext["compile_sdk"] as Int

    defaultConfig {
        applicationId = "com.lianyi.wubbaboo"
        minSdk = rootProject.ext["min_sdk"] as Int
        targetSdk = rootProject.ext["target_sdk"] as Int
        versionCode = rootProject.ext["wubbaboo_version"] as Int
        versionName = rootProject.ext["wubbaboo_version_name"].toString()

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = rootProject.ext["compose_compiler_version"].toString()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation(project(":core"))
}