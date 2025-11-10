plugins {
    id("com.android.application")
    // Ambos plugins de Kotlin necesitan su versión
    id("org.jetbrains.kotlin.android")
    // Este es el ID correcto para el plugin de Compose a partir de Kotlin 2.0
    id("org.jetbrains.kotlin.plugin.compose")
}

android {
    namespace = "mx.edu.utng.aalp.notificaciones"
    compileSdk = 34

    defaultConfig {
        applicationId = "mx.edu.utng.aalp.notificaciones"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    buildFeatures {
        compose = true
    }

    // Ya no se necesita el bloque composeOptions para la versión del compilador
    // porque el nuevo plugin de Compose se encarga de ello.

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("androidx.compose.ui:ui:1.6.0")
    implementation("androidx.compose.material3:material3:1.2.0")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.work:work-runtime-ktx:2.8.1")
    implementation("androidx.core:core:1.12.0")

}