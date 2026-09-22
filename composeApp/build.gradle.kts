import org.gradle.kotlin.dsl.invoke
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    id("com.android.library")
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.jetbrains.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidx.room)
//    alias(libs.plugins.mockative)
}

kotlin {
    jvmToolchain(17)
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    jvm()
    room {
        schemaDirectory("$projectDir/schemas")
    }


    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
//            Added
            implementation(libs.core.splashscreen)
            implementation(libs.bundles.koin.android)
            implementation(libs.ktor.client.okhttp)
            implementation(libs.bundles.coil)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.materialIconsExtended)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
//            Added
            implementation(libs.material.icons.core)
            implementation(libs.bundles.koin.compose)
            implementation(libs.bundles.ktor)
            implementation(libs.bundles.coil)
            implementation(libs.compose.navigation)
            // DataStore library
            implementation(libs.androidx.datastore.preferences.core)
//            room
            implementation(libs.androidx.room.runtime)
            implementation(libs.sqlite.bundled)
//            Image picker
            implementation(libs.cmp.image.pick.n.crop)
            implementation(libs.kmp.date.time.picker)
//            Adaptive
            implementation(libs.material3.adaptive)
            //Logging
            implementation(libs.kermit)
            //Country-Code-Picker
            implementation(libs.country.picker.kmp)
            //DateTime
            implementation(libs.kotlinx.datetime)


        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
//            Added
            implementation(kotlin("test-annotations-common"))
            implementation(libs.assertk)
            implementation(libs.ktor.client.mock)
//            implementation(libs.mockative)
        }
        nativeMain.dependencies {
            implementation(libs.ktor.client.darwin)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutinesSwing)
//          Ktor
            implementation(libs.ktor.client.okhttp)
            implementation(libs.pdfbox)

        }
    }
}

dependencies {
    add("kspAndroid", libs.androidx.room.compiler)
    add("kspIosSimulatorArm64", libs.androidx.room.compiler)
    add("kspIosX64", libs.androidx.room.compiler)
    add("kspIosArm64", libs.androidx.room.compiler)
    add("kspJvm", libs.androidx.room.compiler)
}

android {
    namespace = "com.abdulmateen.pos_offline"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildFeatures{
        buildConfig = true
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    lint {
        targetSdk = 36
    }
    testOptions {
        targetSdk = 36
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

compose.desktop {;
    application {
        mainClass = "com.abdulmateen.pos_offline.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb, TargetFormat.Exe)
            packageName = "TallyTrades"
            packageVersion = "1.0.1"

            // IMPORTANT: DataStore/Protobuf requires sun.misc.Unsafe
            modules("jdk.unsupported")
            // Set the icon for the packaged application
            val iconPath = project.file("packaging/windows/tally_trades_logo.ico")
            windows {
                iconFile.set(iconPath)
                shortcut = true
                menuGroup = "TallyTrades"
            }
            linux.iconFile.set(project.file(iconPath))
            windows.iconFile.set(project.file(iconPath))
            macOS.iconFile.set(project.file(iconPath))
            
            buildTypes.release.proguard {
                isEnabled.set(false)
            }
        }
    }
}
