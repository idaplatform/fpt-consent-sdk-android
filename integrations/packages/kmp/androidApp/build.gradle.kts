import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {id("com.android.application")}
android {
 namespace="vn.fpt.cmp.kmp.example";compileSdk=36
 defaultConfig{applicationId="vn.fpt.cmp.kmp.example";minSdk=23;targetSdk=36;versionCode=1;versionName="0.1.0"}
 compileOptions{sourceCompatibility=JavaVersion.VERSION_11;targetCompatibility=JavaVersion.VERSION_11}
}
dependencies{implementation(project(":shared"))}

kotlin { compilerOptions { jvmTarget.set(JvmTarget.JVM_11) } }
