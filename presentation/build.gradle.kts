plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.fairyband.soak.presentation"
    compileSdk = 36

    defaultConfig {
        minSdk = 28

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        buildConfigField(
            "String",
            "VERSION_NAME",
            "\"${project.findProperty("VERSION_CODE") ?: "3"}\""
        )
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
        buildConfig = true
    }
}

dependencies {
    // module
    implementation(project(":data"))
    implementation(project(":core"))
    implementation(project(":domain"))

    // core
    implementation(libs.androidx.core.ktx)

    // compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.bundles.compose.ui)
    implementation(libs.androidx.compose.saveable)
    implementation(libs.androidx.foundation)
    implementation(libs.kotlinx.collections.immutable)
    debugImplementation(libs.androidx.ui.tooling)

    // lifecycle
    implementation(libs.bundles.lifecycle.compose)

    // navigation
    implementation(libs.androidx.navigation3.runtime)
    implementation(libs.androidx.navigation3.ui)
    implementation(libs.androidx.lifecycle.viewmodel.navigation3)

    // di
    implementation(libs.koin.annotations)
    implementation(libs.koin.core)
    implementation(libs.koin.android)
    implementation(libs.koin.androidx.compose)
    ksp(libs.koin.ksp)

    // serialization
    implementation(libs.kotlinx.serialization.core)

    // google
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics)
    implementation(libs.accompanist.flowlayout)
    implementation(libs.firebase.messaging)
    implementation(libs.firebase.crashlytics)

    // coroutine
    implementation(libs.kotlinx.coroutines.android)

    // log
    implementation(libs.timber)

    // window
    implementation(libs.androidx.window)
    implementation(libs.androidx.window.size)

    // test
    testImplementation(libs.kotest.assertions.core)
    testImplementation(libs.kotest.framework.engine)
    testImplementation(libs.kotest.runner.junit5)
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}
