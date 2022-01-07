pluginManagement {
    val kotlinVersion: String by settings
    val johnrengelmanShadowVersion: String by settings
    val micronautApplicationVersion: String by settings
    val benManesVersionsVersion: String by settings
    val ktlintVersion: String by settings

    plugins {
        kotlin("jvm").version(kotlinVersion)
        kotlin("kapt").version(kotlinVersion)
        id("org.jetbrains.kotlin.plugin.allopen").version(kotlinVersion)
        id("com.github.johnrengelman.shadow").version(johnrengelmanShadowVersion)
        id("io.micronaut.application").version(micronautApplicationVersion)
        id("com.github.ben-manes.versions").version(benManesVersionsVersion)
        id("org.jlleitschuh.gradle.ktlint").version(ktlintVersion)
    }
}

rootProject.name = "idiomcentric-service"
