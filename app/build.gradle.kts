
plugins {
    alias(libs.plugins.android.application)
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.example.studentmvvm"

    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "com.example.studentmvvm"
        minSdk = 24
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }

    buildFeatures {
        viewBinding = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {

    // 1. Android Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.material)

    // 2. RecyclerView & CardView
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.cardview:cardview:1.0.0")

    // 3. Fragment
    implementation("androidx.fragment:fragment-ktx:1.8.3")

    // 4. ViewModel & LiveData
    val lifecycleVersion = "2.8.6"

    implementation(
        "androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion"
    )

    implementation(
        "androidx.lifecycle:lifecycle-livedata-ktx:$lifecycleVersion"
    )

    // 5. Room Database
    val roomVersion = "2.8.5"

    implementation("androidx.room:room-runtime:$roomVersion")
    ksp("androidx.room:room-compiler:$roomVersion")

    // 6. Kotlin Coroutines
    implementation(
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1"
    )

    // 7. Retrofit REST API
    val retrofitVersion = "2.9.0"

    implementation(
        "com.squareup.retrofit2:retrofit:$retrofitVersion"
    )

    implementation(
        "com.squareup.retrofit2:converter-gson:$retrofitVersion"
    )

    implementation(
        "com.squareup.okhttp3:logging-interceptor:4.12.0"
    )

    // 8. Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
