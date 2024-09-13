plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.mandar_app"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.mandar_app"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)


    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")

    implementation("com.android.volley:volley:1.2.1")

    implementation ("com.squareup.picasso:picasso:2.71828")

    // AndroidX dan Material Design
    implementation ("androidx.appcompat:appcompat:1.4.1")
    implementation ("com.google.android.material:material:1.5.0")
    implementation ("androidx.constraintlayout:constraintlayout:2.1.3")

    // RecyclerView
    implementation ("androidx.recyclerview:recyclerview:1.2.1")

    // CardView
    implementation ("androidx.cardview:cardview:1.0.0")

    // Retrofit untuk API calls
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    // Gson untuk JSON parsing
    implementation ("com.google.code.gson:gson:2.8.9")

    // Glide untuk loading gambar
    implementation ("com.github.bumptech.glide:glide:4.12.0")
    annotationProcessor ("com.github.bumptech.glide:compiler:4.12.0")

    // OkHttp untuk networking (opsional, biasanya sudah termasuk dengan Retrofit)
    implementation ("com.squareup.okhttp3:okhttp:4.9.3")

    // Lifecycle components (opsional, tapi disarankan untuk manajemen lifecycle yang lebih baik)
    implementation ("androidx.lifecycle:lifecycle-viewmodel:2.4.1")
    implementation ("androidx.lifecycle:lifecycle-livedata:2.4.1")

    // Dependensi untuk Mockito
    testImplementation ("org.mockito:mockito-core:4.2.0")

    // Dependensi untuk pengujian Android
    androidTestImplementation ("androidx.test.ext:junit:1.1.3")
    androidTestImplementation ("androidx.test.espresso:espresso-core:3.4.0")

    // Dependensi untuk pengujian UI dan komponen Android
    androidTestImplementation ("androidx.test:core:1.4.0")
    androidTestImplementation ("androidx.test:rules:1.4.0")
    androidTestImplementation ("androidx.test:runner:1.4.0")

    //Slide gambar
    implementation ("androidx.viewpager2:viewpager2:1.0.0")
}
