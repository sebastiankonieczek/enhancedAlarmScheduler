plugins {
    idea
    id( "com.android.application" )
    id( "kotlin-android" )
    id( "kotlin-kapt" )
}

idea.module {
    isDownloadSources = true
    isDownloadJavadoc = true
}

android {
    defaultConfig {
        applicationId = "com.example.hammington.myapplication"
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        minSdk = 29
        targetSdk = 30
        compileSdk = 30
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles( getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
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
        dataBinding = true
        viewBinding = true
    }
}

dependencies {
    implementation("com.google.guava:guava:31.0-jre")
    implementation("com.google.code.gson:gson:2.8.8")
    implementation( "androidx.activity:activity-ktx:1.3.1" )
    val roomVersion = "2.3.0"

    implementation( "androidx.room:room-runtime:$roomVersion" )
    kapt("androidx.room:room-compiler:$roomVersion")

    implementation( "androidx.room:room-ktx:$roomVersion" )

    implementation( "androidx.room:room-ktx:$roomVersion" )
    implementation( "androidx.room:room-guava:$roomVersion" )

    implementation( fileTree( mapOf( "dir" to "libs", "include" to listOf("*.jar") )) )
    implementation( "org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.5.21" )
    implementation( "androidx.appcompat:appcompat:1.3.1" )
    implementation("androidx.constraintlayout:constraintlayout:2.1.1")
    implementation( "androidx.legacy:legacy-support-v4:1.0.0" )
    implementation( "androidx.recyclerview:recyclerview:1.2.1" )
    implementation( "com.google.android.material:material:1.4.0" )
    testImplementation( "junit:junit:4.13.2" )
    androidTestImplementation( "androidx.test.ext:junit:1.1.3" )
    androidTestImplementation( "androidx.test.espresso:espresso-core:3.4.0" )

    val lifecycleVersion = "2.3.1"
    val archVersion = "2.1.0"

    // ViewModel
    implementation( "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion" )
    // LiveData
    implementation( "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion" )
    // Lifecycles only (without ViewModel or LiveData)
    implementation( "androidx.lifecycle:lifecycle-runtime-ktx:$lifecycleVersion" )

    // Saved state module for ViewModel
    implementation( "androidx.lifecycle:lifecycle-viewmodel-savedstate:$lifecycleVersion" )

    // alternately - if using Java8, use the following instead of lifecycle-compiler
    implementation( "androidx.lifecycle:lifecycle-common-java8:$lifecycleVersion" )

    // optional - helpers for implementing LifecycleOwner in a Service
    implementation( "androidx.lifecycle:lifecycle-service:$lifecycleVersion" )

    // optional - ProcessLifecycleOwner provides a lifecycle for the whole application process
    implementation( "androidx.lifecycle:lifecycle-process:$lifecycleVersion" )

    // optional - ReactiveStreams support for LiveData
    implementation( "androidx.lifecycle:lifecycle-reactivestreams-ktx:$lifecycleVersion" )

    // optional - Test helpers for LiveData
    testImplementation("androidx.arch.core:core-testing:$archVersion")

    val workVersion = "2.6.0"

    // Kotlin + coroutines
    implementation( "androidx.work:work-runtime-ktx:$workVersion" )

    implementation( "androidx.fragment:fragment-ktx:1.3.6" )
    implementation( "androidx.activity:activity-ktx:1.3.1" )
    implementation( "androidx.fragment:fragment-ktx:1.3.6" )
    implementation( "androidx.activity:activity-ktx:1.3.1" )
}
