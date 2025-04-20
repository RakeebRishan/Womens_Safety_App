plugins {
    alias(libs.plugins.android.application) // Keep this line
    id("com.google.gms.google-services") // Google services plugin for Firebase
}

android {
    namespace = "com.nibm.sos_app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.nibm.sos_app"
        minSdk = 24
        targetSdk = 34
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

    // Enable Firebase integration in the project
    buildFeatures {
        viewBinding = true // Optional but good practice if you're using view binding
    }
}

dependencies {
    // Firebase dependencies
    implementation("com.google.android.gms:play-services-location:18.0.0") // Location services
    implementation("com.google.firebase:firebase-database:20.0.5") // Firebase Database
    implementation("com.google.firebase:firebase-auth:21.0.3") // Firebase Authentication
    implementation(platform("com.google.firebase:firebase-bom:33.7.0")) // Firebase BOM
    implementation("com.google.firebase:firebase-analytics") // Firebase Analytics

    // Android dependencies
    implementation(libs.appcompat) // AppCompat for backward compatibility
    implementation(libs.material) // Material Design components
    implementation(libs.activity) // AndroidX Activity
    implementation(libs.constraintlayout) // ConstraintLayout for flexible UI

    // Firebase authentication and database
    implementation(libs.firebase.auth)
    implementation(libs.firebase.database)

    // Testing dependencies
    testImplementation(libs.junit) // JUnit for unit testing
    androidTestImplementation(libs.ext.junit) // JUnit extension for Android tests
    androidTestImplementation(libs.espresso.core) // Espresso for UI testing
}
