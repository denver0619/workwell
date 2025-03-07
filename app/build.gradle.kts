plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.digiview.workwell"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.digiview.workwell"
        minSdk = 28
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    viewBinding {
        enable = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.legacy.support.v4)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.core.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    // CameraX core library
    val camerax_version = "1.2.0-alpha02"
    implementation("androidx.camera:camera-core:$camerax_version") 

    // CameraX Camera2 extensions
    implementation ("androidx.camera:camera-camera2:$camerax_version")

    // CameraX Lifecycle library
    implementation ("androidx.camera:camera-lifecycle:$camerax_version")

    // CameraX View class
    implementation ("androidx.camera:camera-view:$camerax_version")

    // CameraX video capture library
    implementation ("androidx.camera:camera-video:$camerax_version")

    implementation (libs.ffmpeg.kit.full)

    // WindowManager
    implementation("androidx.window:window:1.1.0-alpha03")

    // Navigation library
    val nav_version = "2.5.3"
    implementation ("androidx.navigation:navigation-fragment-ktx:$nav_version")
    implementation ("androidx.navigation:navigation-ui-ktx:$nav_version")

    // Unit testing
    testImplementation("junit:junit:4.13.2")

    // Instrumented testing
    androidTestImplementation ("androidx.test.ext:junit:1.1.3")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.4.0")

    // MediaPipe Library
    implementation ("com.google.mediapipe:tasks-vision:0.10.14")

    // Import the Firebase BoM
    implementation(platform("com.google.firebase:firebase-bom:33.6.0"))

    // When using the BoM, don't specify versions in Firebase dependencies
    implementation("com.google.firebase:firebase-analytics")

    // Cloudinary
    // All:
    implementation ("com.cloudinary:cloudinary-android:3.0.2")

    // Download + Preprocess:
    implementation ("com.cloudinary:cloudinary-android-download:3.0.2")
    implementation ("com.cloudinary:cloudinary-android-preprocess:3.0.2")

    // ExoPlayer
    implementation("androidx.media3:media3-exoplayer:1.4.1")
    implementation("androidx.media3:media3-exoplayer-dash:1.4.1")
    implementation("androidx.media3:media3-ui:1.4.1")

    // GIF
    implementation ("com.github.bumptech.glide:glide:4.12.0")
}