plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.lianyi.core"
    compileSdk = rootProject.ext["compile_sdk"] as Int

    defaultConfig {
        minSdk = rootProject.ext["min_sdk"] as Int

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
}

dependencies {
    api("androidx.core:core-ktx:1.15.0")
    api("org.jetbrains.kotlin:kotlin-reflect:1.9.24")

    //Compose Activity
    api("androidx.activity:activity-compose:1.9.0")
    api("androidx.appcompat:appcompat:1.6.1")
    api("com.google.android.material:material:1.9.0")
    api("androidx.constraintlayout:constraintlayout:2.1.4")

    val composeVersion = rootProject.ext["compose_version"]
    //Compose
    api("androidx.compose.material:material:$composeVersion")
    api("androidx.compose.ui:ui:$composeVersion")
    api("androidx.compose.ui:ui-tooling-preview:$composeVersion")
    testApi("androidx.compose.ui:ui-tooling:$composeVersion")
    testApi("androidx.compose.ui:ui-test-manifest:composeVersion")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:${composeVersion}")

    //Lifecycle
    val lifecycleVersion = "2.8.0"
    api("androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion")
    api("androidx.lifecycle:lifecycle-process:$lifecycleVersion")

    // For AppWidgets support
    // 暂时无相关需求
//    def glance_version = "1.0.0-alpha05"
//    implementation("androidx.glance:glance-appwidget:$glance_version")
//    implementation("androidx.glance:glance-appwidget:$glance_version")

    //Coil
    val coilVersion = "2.6.0"
    api("io.coil-kt:coil-compose:$coilVersion")
    api("io.coil-kt:coil-gif:$coilVersion")

    //Retrofit2
    val retrofit2Version = "2.11.0"
    api("com.squareup.retrofit2:retrofit:$retrofit2Version")
    api("com.squareup.retrofit2:converter-gson:$retrofit2Version")

    //Room
    val roomVersion = "2.6.1"
    // optional - Kotlin Extensions and Coroutines support for Room
    api("androidx.room:room-ktx:$roomVersion")
    api("androidx.room:room-runtime:$roomVersion")
//    ksp("androidx.room:room-compiler:$roomVersion")

    val appCenterSdkVersion = "5.0.4"
    api("com.microsoft.appcenter:appcenter-analytics:${appCenterSdkVersion}")
    api("com.microsoft.appcenter:appcenter-crashes:${appCenterSdkVersion}")

    //Jsoup
    // https://mvnrepository.com/artifact/org.jsoup/jsoup
    api("org.jsoup:jsoup:1.18.1")

    //Gson
    api("com.google.code.gson:gson:2.11.0")

    //Datastore
    api("androidx.datastore:datastore-preferences:1.0.0")

    //ColorPicker
    api("com.github.skydoves:colorpicker-compose:1.0.8")

    //zxing-lite AndroidX
    api("com.github.jenly1314:zxing-lite:3.1.1")

    //CustomActivityOnCrash
    api("cat.ereza:customactivityoncrash:2.4.0")
}