// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.google.gms.google.services) apply false
    id("org.jetbrains.kotlin.android") version "2.0.21" apply false // Check for latest version
}



buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.gradle) // Check for latest version
        classpath(libs.kotlin.gradle.plugin) // Check for latest version
        classpath(libs.google.services) //Check for latest version
        classpath(libs.hilt.android.gradle.plugin)
    }
}

