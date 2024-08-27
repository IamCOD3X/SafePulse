// This file usually contains buildscript dependencies only
plugins {
    id("com.android.application") version "8.0.2" apply false
    kotlin("android") version "1.9.10" apply false // Replace with your Kotlin version
}

buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:8.5.0")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.10")
    }
}


