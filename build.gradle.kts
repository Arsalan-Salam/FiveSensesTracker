// PROJECT-LEVEL build.gradle.kts

plugins {
    // Android Gradle Plugin
    id("com.android.application") version "8.13.1" apply false
    id("com.android.library") version "8.13.1" apply false

    // Kotlin
    id("org.jetbrains.kotlin.android") version "2.2.21" apply false

    // REQUIRED for Jetpack Compose with Kotlin 2.x+
    id("org.jetbrains.kotlin.plugin.compose") version "2.2.21" apply false

    // Firebase
    id("com.google.gms.google-services") version "4.4.4" apply false
}
