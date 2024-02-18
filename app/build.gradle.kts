import java.util.regex.Pattern.compile

plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.chat"
    compileSdk = 33
    defaultConfig {
        applicationId = "com.example.chat"
        minSdk = 23
        targetSdk = 33

        versionCode = 23
        versionName = "1.8.0"

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
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        buildConfig=true
    }
}

dependencies {
    implementation ("org.java-websocket:Java-WebSocket:1.5.1")
    implementation("com.amap.api:map2d:latest.integration")
    implementation("com.amap.api:location:6.4.1")
    implementation ("org.jsoup:jsoup:1.12.1")
    implementation ("io.noties.markwon:core:4.6.2")
    implementation ("io.noties.markwon:image:4.6.2")
    implementation ("io.noties.markwon:image-glide:4.6.2")
    implementation ("com.squareup.okhttp3:okhttp:3.7.0")
    implementation ("com.squareup.okio:okio:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")
}