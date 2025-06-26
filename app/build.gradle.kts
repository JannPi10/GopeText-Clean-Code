plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
<<<<<<< HEAD
    //esto es firebase
}

android {
    namespace = "com.example.gopetext"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.gopetext"
=======
}

android {
    namespace = "com.danieldaz.registro_gopetext"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.danieldaz.registro_gopetext"
>>>>>>> 785b63d (Task: Register!)
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
<<<<<<< HEAD

=======
//    this library is to use constraintlayout in the views
    implementation(libs.androidx.constraintlayout)

//    default libraries in android studio
>>>>>>> 785b63d (Task: Register!)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
<<<<<<< HEAD
    implementation(libs.androidx.appcompat)
=======
>>>>>>> 785b63d (Task: Register!)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
<<<<<<< HEAD
    implementation(libs.androidx.constraintlayout)
    //esto es firebase
=======
>>>>>>> 785b63d (Task: Register!)
}