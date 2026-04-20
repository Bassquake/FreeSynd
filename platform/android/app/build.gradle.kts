plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.bassquake.freesynd"
    compileSdk = 36
    ndkVersion = "27.3.13750724"

    defaultConfig {
        applicationId = "com.bassquake.freesynd"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "0.958"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        externalNativeBuild {
            cmake {
                // STOP REBUILDING compile_commands.json SO VS STOPS RELOADING
                arguments("-DCMAKE_EXPORT_COMPILE_COMMANDS=OFF")
                cppFlags("")
                // Support 16KB compiling
                arguments += listOf("-DANDROID_SUPPORT_FLEXIBLE_PAGE_SIZES=ON")
                // This ensures the compiler builds for your phone's architecture
                abiFilters.addAll(listOf("arm64-v8a", "x86_64"))
            }
        }
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
    // HIDE OR INCLUDE ASSETS INTO APK
    sourceSets {
        getByName("main") {
            // GAME ASSET FILES PATH
            assets.srcDirs("${project.rootDir}../../../assets")
        }
    }
    externalNativeBuild {
        cmake {
            // SOURCE PATH
            path = file("${project.rootDir}../../../source/CMakeLists.txt")
            version = "3.22.1"
        }
    }
    buildFeatures {
        viewBinding = true
    }
    /* // SET ICONS ETC FOR RELEVANT DEVICES
    florDimensions.add("device")
    productFlavors {
        create("quest") {
            dimension = "device"
        }
        create("mobile") {
            dimension = "device"
        }
    }*/
    // COPY FINAL APK TO build/android FOLDER
    applicationVariants.all {
        val variant = this
        val variantName = variant.name.capitalize()

        variant.outputs
            .map { it as com.android.build.gradle.internal.api.BaseVariantOutputImpl }
            .forEach { output ->
                // 1. Rename the file in the default build folder
                val newName = "freesynd-${variant.versionName}.apk"
                output.outputFileName = newName

                // 2. Create a task to copy it to your target folder
                val copyTask = tasks.register<Copy>("copy${variantName}ApkToFolder") {
                    from(output.outputFile)
                    // Change this path to your "particular folder"
                    into(file("${project.rootDir}/../../build/android"))

                    // Ensure the copy happens after the APK is actually built
                    dependsOn(variant.assembleProvider)
                }

                // 3. Automatically run the copy whenever you build this variant
                variant.assembleProvider.configure {
                    finalizedBy(copyTask)
                }
            }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}