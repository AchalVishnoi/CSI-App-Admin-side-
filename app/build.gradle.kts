plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.csiappcompose"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.csiappcompose"
        minSdk = 26
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
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.material)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)
    implementation(libs.androidx.activity)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation("com.squareup.retrofit2:retrofit:2.9.0") // Retrofit core
    implementation("com.squareup.retrofit2:converter-gson:2.9.0") // Gson converter for JSON parsing
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.2") // Logging interceptor (optional)

    val lifecycle_version = "2.8.7"
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:$lifecycle_version")

    implementation("androidx.datastore:datastore-preferences:1.1.2")

    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")

    implementation ("androidx.fragment:fragment-ktx:1.6.2")
    implementation ("androidx.appcompat:appcompat:1.6.1")
    implementation ("androidx.core:core-ktx:1.12.0")




    val nav_version = "2.8.7"

    implementation("androidx.navigation:navigation-compose:$nav_version")

    implementation("androidx.compose.runtime:runtime-livedata:1.7.8")


    //to add image
//    implementation("io.coil-kt.coil3:coil-compose:3.1.0")

    implementation("io.coil-kt:coil-compose:2.5.0")


    implementation("com.google.ai.client.generativeai:generativeai:0.4.0")

    implementation("com.squareup.okhttp3:okhttp:4.10.0")

    implementation("com.airbnb.android:lottie-compose:6.2.0")

    implementation("androidx.constraintlayout:constraintlayout-compose:1.0.1")

    implementation( "com.google.accompanist:accompanist-navigation-animation:0.31.1-alpha")

    implementation(platform("com.google.firebase:firebase-bom:33.11.0"))
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-messaging:23.1.2")

    implementation("androidx.compose.ui:ui:1.5.1")
    implementation("androidx.compose.material3:material3:1.2.1")
    implementation("androidx.compose.runtime:runtime:1.5.1")
    implementation("androidx.compose.ui:ui-tooling-preview:1.5.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.8.0")
    implementation("androidx.navigation:navigation-compose:2.7.3")
    implementation("androidx.hilt:hilt-navigation-compose:1.1.0")

    implementation("androidx.constraintlayout:constraintlayout-compose:1.1.0")

    implementation(platform("androidx.compose:compose-bom:2023.10.00"))
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.navigation:navigation-compose")


   
        implementation("androidx.compose.foundation:foundation:1.5.1") // Scroll and layout components
        implementation("androidx.compose.material:material:1.5.1") // Material design
        implementation("com.google.accompanist:accompanist-systemuicontroller:0.32.0") // Optional system UI control
        implementation("androidx.compose.material3:material3:1.1.0") // Optional system UI control


    implementation ("androidx.compose.ui:ui:1.0.0")
    implementation( "androidx.compose.material:material:1.0.0")
    implementation ("androidx.activity:activity-compose:1.3.0")





}