import io.gitlab.arturbosch.detekt.Detekt
import io.gitlab.arturbosch.detekt.DetektCreateBaselineTask

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.detekt)
    alias(libs.plugins.ksp) apply false
}

fun Detekt.setupCommonDetektSettings() {
    parallel = true
    autoCorrect = false
    disableDefaultRuleSets = false
    buildUponDefaultConfig = false
    jvmTarget = JavaVersion.VERSION_11.toString()

    setSource(files(rootDir))
    include("**/*.kt")
    include("**/*.kts")
    exclude("**/resources/**")
    exclude("**/build/**")

    reports {
        xml.required.set(true)
        html.required.set(true)
        txt.required.set(true)
        sarif.required.set(false)
    }
}

val detektAll by tasks.register<Detekt>("detektAll") {
    description = "Runs over whole code base without the starting overhead for each module."

    setupCommonDetektSettings()
    config.setFrom(files(rootDir.resolve("conf/detekt.yml")))
}

val detektFormat by tasks.register<Detekt>("detektFormat") {
    description = "Reformats whole code base."

    setupCommonDetektSettings()
    autoCorrect = true
    config.setFrom(files(rootDir.resolve("conf/detekt.yml")))
}

val detektProjectBaseline by tasks.register<DetektCreateBaselineTask>("detektProjectBaseline") {
    description = "Overrides current baseline."

    setSource(files(rootDir))
    include("**/*.kt")
    include("**/*.kts")
    exclude("**/resources/**")
    exclude("**/build/**")

    buildUponDefaultConfig.set(true)
    ignoreFailures.set(true)
    parallel.set(true)
    jvmTarget = JavaVersion.VERSION_11.toString()
    config.setFrom(files(rootDir.resolve("conf/detekt.yml")))
}

dependencies {
    add("detekt", libs.staticAnalysis.detektCli)
    add("detektPlugins", libs.staticAnalysis.detektFormatting)
    add("detektPlugins", libs.staticAnalysis.detektLibraries)
}
