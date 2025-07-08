plugins {
    // library
    id("root.publication")
    alias(libs.plugins.android.library).apply(false)
    alias(libs.plugins.kotlin.multiplatform).apply(false)
    alias(libs.plugins.kotlinx.atomicfu).apply(false)
    
    // sample apps
    alias(libs.plugins.android.application).apply(false)
    alias(libs.plugins.kotlin.compose).apply(false)
    alias(libs.plugins.jetbrains.compose).apply(false)
}
