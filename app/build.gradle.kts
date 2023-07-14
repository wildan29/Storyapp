import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    id("com.android.application")
    kotlin("android")
    id("kotlin-parcelize")
    kotlin("kapt")
    id("com.google.dagger.hilt.android")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
}

// define major, minor, patch for version code and version name
val major = 1
val minor = 0
val patch = 0

android {
    namespace = "com.dicoding.storyapp"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.dicoding.storyapp"
        minSdk = 21
        targetSdk = 33
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


            // add manifest placeholder
            manifestPlaceholders["MAP_API"] = "${properties["MAP_API_KEY"]}"
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

            // add manifest placeholder
            manifestPlaceholders["MAP_API"] = "${properties["MAP_API_KEY"]}"
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
    implementation("com.google.dagger:hilt-android:2.44")
    kapt("com.google.dagger:hilt-android-compiler:2.44")
    kapt("androidx.hilt:hilt-compiler:1.0.0")
    implementation("android.arch.lifecycle:extensions:1.1.1")

    //Logging
    implementation("com.jakewharton.timber:timber:5.0.1")
    implementation("com.orhanobut:logger:2.2.0")

    // splash screen
    implementation("androidx.core:core-splashscreen:1.0.1")

    // navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")

    // Preference Data Store
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // Corutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.5")

    // Coroutine Lifecycle Scopes
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.2.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.1")

    // Activity KTX for viewModels()
    implementation("androidx.activity:activity-ktx:1.1.0")

    // retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")

    //Circle Image
    implementation("de.hdodenhof:circleimageview:3.1.0")

    // glide
    implementation("com.github.bumptech.glide:glide:4.12.0")

    //drawer layout
    implementation("androidx.drawerlayout:drawerlayout:1.1.1")

    // circle image view
    implementation("de.hdodenhof:circleimageview:3.1.0")

    // camera
    implementation("androidx.camera:camera-camera2:1.2.1")
    implementation("androidx.camera:camera-lifecycle:1.2.1")
    implementation("androidx.camera:camera-view:1.2.1")

    // paging 3
    implementation("androidx.paging:paging-runtime-ktx:3.1.1")

    // google maps
    implementation("com.google.android.gms:play-services-maps:18.1.0")

    // Room
    implementation("androidx.room:room-ktx:2.5.1")
    kapt("androidx.room:room-compiler:2.5.1")
    implementation("androidx.room:room-paging:2.5.1")

    // test implementation
    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.arch.core:core-testing:2.2.0")

    // TestDispatcher
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.6.4")

    // Mockito
    testImplementation("org.mockito:mockito-core:3.12.4")
    testImplementation("org.mockito:mockito-inline:3.12.4")

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