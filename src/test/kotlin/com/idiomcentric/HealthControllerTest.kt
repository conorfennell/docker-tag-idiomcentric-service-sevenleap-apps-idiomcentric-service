package com.idiomcentric

import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

@MicronautTest(packages = ["com.idiomcentric"])
class HealthControllerTest {

    @Inject
    @field:Client("/v1")
    lateinit var healthClient: HttpClient


    @Test
    fun testHealth() {
        val actual: Health =
            healthClient.toBlocking().retrieve(HttpRequest.GET<Health>("health"), Health::class.java)

        Assertions.assertNotNull(actual, "health should not be null")
    }
}

