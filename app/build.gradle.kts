import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-parcelize")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("androidx.navigation.safeargs.kotlin")
}

// define major, minor, patch for version code and version name
val major = 1
val minor = 0
val patch = 0

android {
    namespace = "com.dicoding.storyapp"
    compileSdk = 32

    defaultConfig {
        applicationId = "com.dicoding.storyapp"
        minSdk = 21
        targetSdk = 32
        versionCode = (major * 10000) + (minor * 100) + patch
        versionName = "$major.$minor.$patch"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        getByName("release") {
            isDebuggable = false

            // Enables code shrinking, obfuscation, and optimization for only
            // your project's release build type. Make sure to use a build
            // variant with `isDebuggable=false`.
            isMinifyEnabled = true

            // Enables resource shrinking, which is performed by the
            // Android Gradle plugin.
            isShrinkResources = true

            // Includes the default ProGuard rules files that are packaged with
            // the Android Gradle plugin. To learn more, go to the section about
            // R8 configuration files.
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

            val properties = Properties().apply {
                load(rootProject.file("/config/release.properties").reader())
            }

            // build config for BASE_URL
            buildConfigField(
                "String",
                "BASE_URL",
                "${properties["BASE_URL"]}"
            )
        }

        getByName("debug") {
            isShrinkResources = false
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
            isDebuggable = true
            isMinifyEnabled = false

            val properties = Properties().apply {
                load(rootProject.file("/config/debug.properties").reader())
            }

            // build config for BASE_URL
            buildConfigField(
                "String",
                "BASE_URL",
                "${properties["BASE_URL"]}"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    hilt {
        enableAggregatingTask = true
    }
}

// Allow references to generated code
kapt {
    correctErrorTypes = true
}

dependencies {
    // default implementation
    implementation("androidx.core:core-ktx:1.7.0")
    implementation("androidx.appcompat:appcompat:1.5.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")

    // Dagger-hilt
    implementation("com.google.dagger:hilt-android:2.42")
    kapt("com.google.dagger:hilt-android-compiler:2.42")
    kapt("androidx.hilt:hilt-compiler:1.0.0")
    implementation("android.arch.lifecycle:extensions:1.1.1")

    //Logging
    implementation("com.jakewharton.timber:timber:5.0.1")
    implementation("com.orhanobut:logger:2.2.0")

    // splash screen
    implementation("androidx.core:core-splashscreen:1.0.1")

    // test implementation
    testImplementation("junit:junit:4.13.2")

    // android test implementation
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
}

// copy debug apk when build project is done
tasks.register("copyAPKDebug", Copy::class) {
    dependsOn("test")
    val soureDir = layout.buildDirectory.dir("outputs/apk/debug/app-debug.apk")
    val destDir = "$rootDir/apk"
    from(soureDir)
    into(destDir)
    rename("app-debug.apk", "storyAppDebug.apk")

    // Untuk cek apakah aplikasi ada virus atau tidak, bisa diliath dari MD5 yang sudah di generate
    doLast {
        val filePath = File(destDir, "storyAppDebug.apk")
        ant.withGroovyBuilder {
            "checksum"("file" to filePath.path)
        }
    }
}

tasks.whenTaskAdded {
    if (this.name == "assembleDebug") {
        this.finalizedBy("copyAPKDebug")
    }
}