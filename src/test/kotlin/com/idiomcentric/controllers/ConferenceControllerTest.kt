package com.idiomcentric.controllers

import com.idiomcentric.Conference
import com.idiomcentric.IntegrationProvider
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ConferenceControllerTest : IntegrationProvider() {

    @Inject
    @field:Client("/conferences")
    lateinit var conferenceClient: HttpClient

    @Test
    fun shouldReturnAllConferencesSuccessfully() {
        val actual: List<Conference> = conferenceClient
            .toBlocking()
            .retrieve(HttpRequest.GET<List<Conference>>("/all"), Argument.listOf(Conference::class.java))

        Assertions.assertEquals(1, actual.size, "should return 1 conference")
    }

    @Test
    fun shouldReturnConferenceByIdSuccessfully() {
        val conferences: List<Conference> = conferenceClient
            .toBlocking()
            .retrieve(HttpRequest.GET<List<Conference>>("/all"), Argument.listOf(Conference::class.java))

        Assertions.assertEquals(1, conferences.size, "should return 1 conference")

        val actual: List<Conference> = conferenceClient
            .toBlocking()
            .retrieve(HttpRequest.GET<List<Conference>>("/${conferences.first().id}"), Argument.listOf(Conference::class.java))

        Assertions.assertEquals(1, actual.size, "should return 1 conference")
    }
}
