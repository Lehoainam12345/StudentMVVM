
plugins {
    alias(libs.plugins.android.application) apply false

    // Plugin KSP để Room tự động sinh code
    id("com.google.devtools.ksp") version "2.3.10" apply false
}
