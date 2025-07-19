plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
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
        vectorDrawables.useSupportLibrary = true

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
    // Android UI
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)

    // Firebase
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.database)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    // UI Component Libraries
    implementation(libs.material.v130alpha03)
    implementation(libs.viewpager2)
    implementation("com.makeramen:roundedimageview:2.3.0")
    implementation("com.google.android.flexbox:flexbox:3.0.0")

    // Google Play Services
    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)

    // Retrofit + Gson for Networking
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    // Glide - để hiển thị ảnh từ URL như Cloudinary
    implementation("com.github.bumptech.glide:glide:4.16.0")
    annotationProcessor("com.github.bumptech.glide:compiler:4.16.0")

    // Swipe Layout UI
    implementation("com.daimajia.swipelayout:library:1.2.0@aar")
    implementation("com.nineoldandroids:library:2.4.0")

    // AppCompat (đã dùng ở trên nhưng vẫn giữ cho an toàn)
    implementation("androidx.appcompat:appcompat:1.6.1")
}
