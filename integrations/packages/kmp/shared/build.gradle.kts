import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("multiplatform")
    id("com.android.kotlin.multiplatform.library")
}
kotlin {
    android {
        namespace = "vn.fpt.cmp.kmp"
        compileSdk = 36
        minSdk = 23
        compilerOptions { jvmTarget.set(JvmTarget.JVM_11) }
    }
    if (System.getProperty("os.name").contains("Mac")) {
        listOf(iosArm64(), iosSimulatorArm64(), iosX64()).forEach {
            it.binaries.framework { baseName = "SharedCmp" }
        }
    }
    sourceSets {
        androidMain.dependencies { implementation("vn.fpt.cmp:mobile-bridge:0.1.0") }
    }
}
