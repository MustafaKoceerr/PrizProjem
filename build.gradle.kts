// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.10" apply false
    // kendi eklemelerim

    kotlin("jvm") version "1.9.22"
    kotlin("plugin.serialization") version "1.9.22"
}
apply(plugin = "kotlin-kapt")

buildscript {
    repositories {
        google()
        mavenCentral()

    }
    dependencies {
        val nav_version = "2.7.6"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")

        val kotlinVersion = "1.9.21"
        classpath(kotlin("gradle-plugin", version = kotlinVersion))
        classpath(kotlin("serialization", version = kotlinVersion))

    }
}
