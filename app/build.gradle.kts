
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "com.pixelresize.pro"
    compileSdk = 37

    defaultConfig {
        applicationId = "com.pixelresize.pro"

        minSdk = 23
        targetSdk = 36

        versionCode = 1
        versionName = "1.0.0"
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlin {
        compilerOptions {
            jvmTarget.set(
                org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
            )
        }
    }
}

dependencies {
    implementation(platform("androidx.compose:compose-bom:2026.10.00"))

    implementation("androidx.activity:activity-compose:1.12.0")

    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.material:material-icons-extended")

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.10.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.10.0")

    implementation("io.coil-kt.coil3:coil-compose:3.3.0")
    implementation("androidx.exifinterface:exifinterface:1.4.2")

    debugImplementation("androidx.compose.ui:ui-tooling")
}
