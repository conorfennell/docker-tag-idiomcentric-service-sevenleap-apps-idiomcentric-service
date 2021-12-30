pluginManagement {
    val kotlin_version: String by settings
    val johnrengelman_shadow_version: String by settings
    val micronaut_application_version: String by settings
    val ben_manes_versions_version: String by settings

    plugins {
        kotlin("jvm").version(kotlin_version)
        kotlin("kapt").version(kotlin_version)
        id("org.jetbrains.kotlin.plugin.allopen").version(kotlin_version)
        id("com.github.johnrengelman.shadow").version(johnrengelman_shadow_version)
        id("io.micronaut.application").version(micronaut_application_version)
        id("com.github.ben-manes.versions").version(ben_manes_versions_version)
    }
}

rootProject.name="idiomcentric-service"
