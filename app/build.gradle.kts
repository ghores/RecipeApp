plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("com.google.devtools.ksp")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id("androidx.navigation.safeargs")
}

android {
    namespace = "com.example.recipeapp"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.recipeapp"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

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
        buildFeatures {
            viewBinding = true
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    //Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    //Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.9")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.9")
    //Architecture components
    implementation ("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    implementation ("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.8.7")
    implementation ("androidx.lifecycle:lifecycle-livedata-ktx:2.8.7")
    //Coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
    //Room database
    implementation("androidx.room:room-runtime:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")
    //Dagger - Hilt
    implementation("com.google.dagger:hilt-android:2.52")
    kapt("com.google.dagger:hilt-compiler:2.52")
    kapt("androidx.hilt:hilt-compiler:1.2.0")
    //Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.11.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.11.0")
    //OkHTTP client
    implementation ("com.squareup.okhttp3:okhttp:4.12.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.10.0")
    //Image Loading
    implementation ("io.coil-kt:coil:2.7.0")
    //Gson
    implementation ("com.google.code.gson:gson:2.11.0")
    //DataStore
    implementation ("androidx.datastore:datastore-preferences:1.0.0")
    //Calligraphy
    implementation ("io.github.inflationx:calligraphy3:3.1.1")
    implementation ("io.github.inflationx:viewpump:2.1.1")
    //Other
    implementation ("com.facebook.shimmer:shimmer:0.5.0")
    implementation ("com.tokars:shimmer-recyclerview:0.0.5")
    implementation ("com.airbnb.android:lottie:5.2.0")
    implementation ("com.github.MrNouri:DynamicSizes:1.0")
    implementation ("kr.co.prnd:readmore-textview:2.0.1")
}
kapt {
    correctErrorTypes = true
}