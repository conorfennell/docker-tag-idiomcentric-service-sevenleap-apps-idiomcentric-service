val junit_jupiter_engine: String by project
val kotlin_logging_version: String by project
val kotlin_version: String by project
val kotlinx_coroutines_version: String by project
val logback_version: String by project
val micronaut_test_junit5: String by project
val micronaut_version: String by project

plugins {
    kotlin("jvm")
    kotlin("kapt")
    id("org.jetbrains.kotlin.plugin.allopen")
    id("com.github.johnrengelman.shadow")
    id("io.micronaut.application")
    id("com.github.ben-manes.versions")
}

version = "1.0.0"
group = "com.idiomcentric"

repositories {
    mavenCentral()
}

micronaut {
    version.set(micronaut_version)
    runtime("netty")
    testRuntime("junit5")
    processing {
        incremental.set(true)
        annotations.add("com.idiomcentric.*")
    }
}


allOpen {
    // Mark any classes with the following transactions as `open` automatically.
    annotations("io.micronaut.retry.annotation.Retryable")
}
dependencies {
    kapt("io.micronaut:micronaut-http-validation")
    kapt("io.micronaut.openapi:micronaut-openapi")
    compileOnly("org.graalvm.nativeimage:svm")
    implementation("io.swagger.core.v3:swagger-annotations")
    implementation("io.micronaut:micronaut-http-client")
    implementation("io.micronaut:micronaut-runtime")
    implementation("io.micronaut:micronaut-graal")
    implementation("io.micronaut:micronaut-validation")
    implementation("io.micronaut.cache:micronaut-cache-caffeine")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("javax.annotation:javax.annotation-api")
    implementation("org.jetbrains.kotlin:kotlin-reflect:$kotlin_version")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlin_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinx_coroutines_version")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive:$kotlinx_coroutines_version")
    runtimeOnly("ch.qos.logback:logback-classic")
    implementation("ch.qos.logback.contrib:logback-json-classic:$logback_version")
    implementation("ch.qos.logback.contrib:logback-jackson:$logback_version")
    implementation("io.github.microutils:kotlin-logging:$kotlin_logging_version")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("io.micronaut.test:micronaut-test-junit5:$micronaut_test_junit5")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junit_jupiter_engine")
    runtimeOnly("com.fasterxml.jackson.module:jackson-module-kotlin")

}

application {
    mainClass.set("com.idiomcentric.Application")
}

kotlin {
    jvmToolchain {
        (this as JavaToolchainSpec)
        this.languageVersion.set(JavaLanguageVersion.of(11))
    }
}
