import java.util.Properties
import java.io.FileInputStream

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}
val localProperties = Properties().apply {
    val file = rootProject.file("local.properties")
    if (file.exists()) {
        load(FileInputStream(file))
    }
}
val googleMapsKey = localProperties.getProperty("google.maps.key") ?: ""
val baseUrl = localProperties.getProperty("base.url") ?: "http://10.0.3.2:8080/"

android {
    namespace = "com.example.productsaleprm"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.productsaleprm"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
        vectorDrawables.useSupportLibrary = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "GOOGLE_MAPS_API_KEY", "\"$googleMapsKey\"")
        manifestPlaceholders["googleMapsApiKey"] = googleMapsKey

        buildConfigField("String", "BASE_URL", "\"$baseUrl\"")
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
        buildConfig = true

    }
}



dependencies {
    implementation("com.google.firebase:firebase-messaging:23.4.0")
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.database)
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
    implementation ("com.google.android.flexbox:flexbox:3.0.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation ("com.makeramen:roundedimageview:2.3.0")
    implementation ("com.auth0.android:jwtdecode:2.0.1")
    implementation ("com.journeyapps:zxing-android-embedded:4.3.0")

    // Navigtaiton
    implementation (libs.navigation.fragment.ktx)
    implementation (libs.navigation.ui.ktx)
}
