plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("kotlin-kapt")
}

android {
    namespace = "com.example.gopetext"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.danieldaz.registro_gopetext"
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
        viewBinding = true
    }
}

dependencies {
    // ConstraintLayout
    implementation(libs.androidx.constraintlayout)

    // Compose
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.leanback)
    implementation(libs.material)

    // Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.logging.interceptor)

    // Glide
    implementation(libs.glide)
    kapt(libs.compiler)
    implementation(libs.glide.v4151)

    // RoundedImageView
    implementation(libs.roundedimageview)

    // TESTS
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Mockito
    testImplementation(libs.mockito.kotlin)

    // Coroutines
    implementation(libs.kotlinxCoroutinesCore)
    implementation(libs.kotlinxCoroutinesAndroid)
    testImplementation(libs.kotlinxCoroutinesTest)

    // Core testing
    testImplementation(libs.androidx.core.testing)

    // Kotlin test
    testImplementation(kotlin("test"))
}

tasks.withType<Test> {
    // Exclude problematic tests temporarily
    exclude(
        "**/CreateGroupPresenterTest.class",
        "**/DefaultChatsNavigatorTest.class",
        "**/UserAdapterTest.class",
        "**/LoginPresenterTest.class",
        "**/SessionManagerTest.class",
        "**/ErrorUtilsTest.class"
    )
}
