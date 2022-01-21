package com.idiomcentric.controllers

import com.idiomcentric.Conference
import com.idiomcentric.CreateConference
import com.idiomcentric.IntegrationProvider
import com.idiomcentric.PatchConference
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.util.UUID

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
            .retrieve(HttpRequest.GET<List<Conference>>(""), Argument.listOf(Conference::class.java))

        Assertions.assertEquals(1, actual.size, "should return 1 conference")
    }

    @Test
    fun shouldReturnConferenceByIdSuccessfully() {
        val conferences: List<Conference> = conferenceClient
            .toBlocking()
            .retrieve(HttpRequest.GET<List<Conference>>(""), Argument.listOf(Conference::class.java))

        Assertions.assertEquals(1, conferences.size, "should return 1 conference")

        val actual: Conference = conferenceClient
            .toBlocking()
            .retrieve(HttpRequest.GET<Conference>("/${conferences.first().id}"), Conference::class.java)

        Assertions.assertEquals(conferences.first(), actual, "should return 1 conference")
    }

    @Test
    fun shouldReturn400onMalformedUUID() {
        val thrown = Assertions.assertThrows(
            HttpClientResponseException::class.java,
            {
                conferenceClient
                    .toBlocking()
                    .exchange(HttpRequest.GET<Nothing>("/baduuid"), Conference::class.java)
            },
            "HttpClientResponseException Bad Request expected"
        )

        Assertions.assertEquals("Bad Request", thrown.message)
    }

    @Test
    fun shouldReturnConferenceNotFound() {
        val thrown = Assertions.assertThrows(
            HttpClientResponseException::class.java,
            {
                conferenceClient
                    .toBlocking()
                    .retrieve(HttpRequest.GET<Conference>("/${UUID.randomUUID()}"))
            },
            "HttpClientResponseException Not Found expected"
        )

        Assertions.assertEquals("Not Found", thrown.message)
    }

    @Test
    fun shouldCreateConference() {
        val conference: Conference = conferenceClient
            .toBlocking()
            .retrieve(HttpRequest.POST<CreateConference>("", CreateConference("test")), Conference::class.java)

        val actual: Conference = conferenceClient
            .toBlocking()
            .retrieve(HttpRequest.GET<Conference>("/${conference.id}"), Conference::class.java)

        Assertions.assertEquals(conference, actual, "should return 1 conference")
    }

    @Test
    fun shouldUpdateConference() {
        val conference: Conference = conferenceClient
            .toBlocking()
            .retrieve(HttpRequest.POST("", CreateConference("test")), Conference::class.java)

        val updatedName = "update-test"

        conferenceClient
            .toBlocking()
            .exchange<Conference, Nothing>(HttpRequest.PUT("", conference.copy(name = updatedName)))

        val actual: Conference = conferenceClient
            .toBlocking()
            .retrieve(HttpRequest.GET<Conference>("/${conference.id}"), Conference::class.java)

        Assertions.assertEquals(updatedName, actual.name, "should have the name updated for conference")
    }

    @Test
    fun shouldPatchConference() {
        val conference: Conference = conferenceClient
            .toBlocking()
            .retrieve(HttpRequest.POST("", CreateConference("test")), Conference::class.java)

        val updatedName = "update-test"

        conferenceClient
            .toBlocking()
            .exchange<PatchConference, Nothing>(HttpRequest.PATCH("", PatchConference(conference.id, updatedName)))

        val actual: Conference = conferenceClient
            .toBlocking()
            .retrieve(HttpRequest.GET<Conference>("/${conference.id}"), Conference::class.java)

        Assertions.assertEquals(updatedName, actual.name, "should have the name updated for conference")
    }

    @Test
    fun shouldHeadConference() {
        val conference: Conference = conferenceClient
            .toBlocking()
            .retrieve(HttpRequest.POST("", CreateConference("test")), Conference::class.java)

        val headRequest: HttpRequest<*> = HttpRequest.HEAD("/head/${conference.id}")

        val response = conferenceClient.toBlocking().exchange(headRequest, ByteArray::class.java)

        Assertions.assertEquals(response.status, HttpStatus.OK, "should return 200")
    }
}
