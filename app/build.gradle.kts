plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.google.service)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.nexters.knownknowns"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.nexters.knownknowns"
        minSdk = 28
        targetSdk = 36
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
        debug {
            isDebuggable = true
            applicationIdSuffix = ".debug"
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

ksp {
    arg("KOIN_CONFIG_CHECK","true")
}

dependencies {
    // module
    implementation(project(":presentation"))
    implementation(project(":data"))

    // firebase
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)

    // di
    implementation(libs.koin.annotations)
    ksp(libs.koin.ksp)
}
