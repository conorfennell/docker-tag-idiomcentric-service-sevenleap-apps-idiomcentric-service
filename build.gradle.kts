plugins {
    kotlin("jvm") version "1.6.0"
    kotlin("kapt") version "1.6.0"
    id("org.jetbrains.kotlin.plugin.allopen") version "1.6.0"
    id("com.github.johnrengelman.shadow") version "7.1.0"
    id("io.micronaut.application") version "3.0.1"
}

version = "1.0.0"
group = "com.idiomcentric"

repositories {
    mavenCentral()
}

micronaut {
    version.set("3.2.1")
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
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.6.0")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive:1.6.0")
    runtimeOnly("ch.qos.logback:logback-classic")
    implementation("ch.qos.logback.contrib:logback-json-classic:0.1.5")
    implementation("ch.qos.logback.contrib:logback-jackson:0.1.5")
    implementation("io.github.microutils:kotlin-logging:2.1.21")
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation("io.micronaut.test:micronaut-test-junit5:3.0.5")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
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
