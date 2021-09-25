// Top-level build file where you can add configuration options common to all sub-projects/modules.


plugins{
    idea
}

buildscript {
    val kotlinVersion = "1.5.31"
    repositories {
        google()
        mavenCentral()
        maven("https://plugins.gradle.org/m2/")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.0.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

idea.module {
    isDownloadSources = true
    isDownloadJavadoc = true
}

tasks.register("clean",Delete::class){
    delete(rootProject.buildDir)
}
