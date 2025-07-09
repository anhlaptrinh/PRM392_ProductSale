plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.productsaleprm"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.productsaleprm"
        minSdk = 24
        targetSdk = 35
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
    buildFeatures {
        viewBinding = true
        dataBinding = true
    }
}



dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation (libs.material.v130alpha03)
    implementation (libs.viewpager2)
    implementation (libs.glide)
    annotationProcessor (libs.compiler)

//    Google Play Services
    implementation (libs.play.services.maps)
    implementation (libs.play.services.location)

    // Retrofit for networking
    implementation (libs.retrofit)

// Gson converter for Retrofit
    implementation (libs.converter.gson)
    implementation("com.daimajia.swipelayout:library:1.2.0@aar")
    implementation ("com.nineoldandroids:library:2.4.0")


}