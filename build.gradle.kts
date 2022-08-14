plugins {
    kotlin("jvm")
    kotlin("kapt")
    jacoco
    id("com.github.ben-manes.versions")
    id("com.github.johnrengelman.shadow")
    id("io.micronaut.application")
    id("org.jetbrains.kotlin.plugin.allopen")
    id("org.jlleitschuh.gradle.ktlint")
}

version = "1.0.0"
group = "com.idiomcentric"

repositories {
    mavenCentral()
}

micronaut {
    version.set(libs.versions.micronaut)
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
    kapt("io.micronaut.security:micronaut-security-annotations")
    implementation("io.micronaut.sql:micronaut-jdbc-hikari")
    implementation("io.micronaut.security:micronaut-security-jwt")
    implementation(libs.jwks.rsa)
    implementation(libs.logback.jackson)
    implementation(libs.logback.json.classic)
    implementation(libs.logback.classic)
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation(libs.kotlin.logging)
    implementation("io.micronaut.flyway:micronaut-flyway")
    implementation(libs.exposed.core)
    implementation(libs.exposed.dao)
    implementation(libs.exposed.jdbc)
    implementation(libs.exposed.java.time)
    implementation("io.micronaut:micronaut-http-client")
    implementation("io.micronaut:micronaut-runtime")
    implementation("io.micronaut:micronaut-graal")
    implementation("io.micronaut:micronaut-validation")
    implementation("io.micronaut.cache:micronaut-cache-caffeine")
    implementation("io.micronaut.kotlin:micronaut-kotlin-runtime")
    implementation("io.micronaut.reactor:micronaut-reactor")
    implementation("io.micronaut.security:micronaut-security-jwt")
    implementation("io.swagger.core.v3:swagger-annotations")
    implementation("javax.annotation:javax.annotation-api")
    implementation("org.graalvm.nativeimage:svm")
    implementation(libs.kotlin.reflect)
    implementation(libs.kotlin.stdlib.jdk8)
    implementation(libs.kotlinx.coroutines.reactive)
    implementation(libs.kotlinx.coroutines.core)
    runtimeOnly("org.postgresql:postgresql")

    testImplementation(libs.kotest.property)
    testImplementation(libs.micronaut.test.junit5)
    testImplementation(libs.junit.jupiter.engine)
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testImplementation(libs.mockk)
    testImplementation(libs.testcontainers.mockserver)
    testImplementation(libs.testcontainers.postgresql)
    testAnnotationProcessor("io.micronaut:micronaut-inject-java")
    testImplementation(libs.mockserver.client.java)
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

tasks.jacocoTestReport {
    reports {
        xml.required.set(true)
        csv.required.set(true)
    }
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport) // report is always generated after tests run
}
tasks.jacocoTestReport {
    dependsOn(tasks.test) // tests are required to run before generating the report
}

nativeBuild {
    buildArgs.add("--initialize-at-run-time=org.postgresql.sspi.SSPIClient")
    buildArgs.add("--report-unsupported-elements-at-runtime")
    buildArgs.add("--initialize-at-build-time=org.postgresql.Driver,org.postgresql.util.SharedTimer")
}
