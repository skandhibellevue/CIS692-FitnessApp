plugins {
    alias(libs.plugins.android.application) // Ensure `libs.plugins.android.application` exists in your `gradle/libs.versions.toml`.
    alias(libs.plugins.kotlin.android) // Ensure `libs.plugins.kotlin.android` exists in your `gradle/libs.versions.toml`.
}

android {
    namespace = "com.example.fitnessapp"
    compileSdk = 35

    buildFeatures {
        compose = true // Enable Jetpack Compose
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.15" // Ensure this matches your Compose version
    }

    defaultConfig {
        applicationId = "com.example.fitnessapp"
        minSdk = 25
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        multiDexEnabled = true
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
}

dependencies {
    // Core dependencies
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)

    // Jetpack Compose dependencies
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.activity.compose) // Add this line
    implementation("com.kizitonwose.calendar:view:2.3.0")
    implementation("com.kizitonwose.calendar:compose:2.3.0")

    coreLibraryDesugaring(libs.desugar.jdk.libs)
    // Material Components
    implementation(libs.material)

    // Testing dependencies
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}