plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.example.baydesigns"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.baydesigns"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
    buildFeatures {
        compose = true
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
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // 3D SceneView
    implementation (libs.arsceneview)
    implementation (libs.core)
//    implementation ("com.google.ar.sceneform:core:1.17.1")
//    implementation ("com.google.ar.sceneform.ux:sceneform-ux:1.17.1")
//    implementation ("com.google.ar.sceneform.ux:sceneform-ux:1.17.1") // Check for the latest version

    // Extended icons
    implementation (libs.androidx.material.icons.extended)

    //navigation
    implementation (libs.androidx.navigation.compose)

    // CameraX
    implementation ("androidx.camera:camera-core:1.4.1")
    implementation ("androidx.camera:camera-camera2:1.4.1")
    implementation ("androidx.camera:camera-lifecycle:1.4.1")
    implementation ("androidx.camera:camera-view:1.4.1")


  //Plette
    implementation ("androidx.palette:palette-ktx:1.0.0")
    // AndroidX
    implementation ("androidx.lifecycle:lifecycle-common-java8:2.8.7")

}