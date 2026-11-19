plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "br.edu.unoeste.vagasonline"
    compileSdk {
        version = release(37)
    }

    defaultConfig {
        applicationId = "br.edu.unoeste.vagasonline"
        minSdk = 23
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.recyclerview)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
}
