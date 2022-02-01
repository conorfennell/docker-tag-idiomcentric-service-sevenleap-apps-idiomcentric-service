package com.idiomcentric.controllers

import com.idiomcentric.Conference
import com.idiomcentric.CreateConference
import com.idiomcentric.IntegrationProvider
import com.idiomcentric.PatchConference
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.instant
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.string
import io.kotest.property.arbitrary.uuid
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.http.netty.cookies.NettyCookie
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.time.Duration
import java.time.Instant
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

        Assertions.assertEquals(2, actual.size, "should return 2 conference")
    }

    @Test
    fun shouldReturnOneConferencesSuccessfully() {
        val actual: List<Conference> = conferenceClient
            .toBlocking()
            .retrieve(HttpRequest.GET<List<Conference>>("?limit=1"), Argument.listOf(Conference::class.java))

        Assertions.assertEquals(1, actual.size, "should return 1 conference")
    }

    @Test
    fun shouldReturnConferenceByIdSuccessfully() {
        val conferences: List<Conference> = conferenceClient
            .toBlocking()
            .retrieve(HttpRequest.GET<List<Conference>>(""), Argument.listOf(Conference::class.java))

        Assertions.assertEquals(2, conferences.size, "should return 2 conference")

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
        val createConference = ArbCreateConference.next()

        repeat(10) {
            conferenceClient
                .toBlocking()
                .retrieve(HttpRequest.POST<CreateConference>("", createConference), Conference::class.java)
        }

        val conferences: List<Conference> = conferenceClient
            .toBlocking()
            .retrieve(HttpRequest.GET<List<Conference>>(""), Argument.listOf(Conference::class.java))

        Assertions.assertEquals(10, conferences.size, "should return 10 conference")
    }

    @Test
    fun shouldUpdateConference() {
        val conference: Conference = conferenceClient
            .toBlocking()
            .retrieve(HttpRequest.POST("", CreateConference("test", Instant.now())), Conference::class.java)

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
            .retrieve(HttpRequest.POST("", CreateConference("test", Instant.now())), Conference::class.java)

        val updatedName = "update-test"

        conferenceClient
            .toBlocking()
            .exchange<PatchConference, Nothing>(HttpRequest.PATCH("", PatchConference(conference.id, updatedName, Instant.now())))

        val actual: Conference = conferenceClient
            .toBlocking()
            .retrieve(HttpRequest.GET<Conference>("/${conference.id}"), Conference::class.java)

        Assertions.assertEquals(updatedName, actual.name, "should have the name updated for conference")
    }

    @Test
    fun shouldHeadConference() {
        val conference: Conference = conferenceClient
            .toBlocking()
            .retrieve(HttpRequest.POST("", CreateConference("test", Instant.now())), Conference::class.java)

        val headRequest: HttpRequest<*> = HttpRequest.HEAD("/${conference.id}")

        val response = conferenceClient.toBlocking().exchange(headRequest, ByteArray::class.java)

        Assertions.assertEquals(response.status, HttpStatus.OK, "should return 200")
    }

    @Test
    fun shouldReturnConflictUsingStatusAnnotation() {
        val thrown = Assertions.assertThrows(
            HttpClientResponseException::class.java,
            {
                conferenceClient
                    .toBlocking()
                    .exchange(HttpRequest.GET<Nothing>("/conflict"), ByteArray::class.java)
            },
            "HttpClientResponseException Conflict"
        )

        Assertions.assertEquals("Conflict", thrown.message)
    }

    @Test
    fun shouldReturnSetCookie() {
        val request = HttpRequest
            .GET<Nothing>("/cookie")
            .cookie(NettyCookie("simple", "shouldReturnSetCookie").httpOnly(true))

        val response = conferenceClient
            .toBlocking()
            .exchange(request, String::class.java)

        Assertions.assertEquals("shouldReturnSetCookie", response.body())
    }

    @Test
    fun shouldReturnSetHeader() {
        val request = HttpRequest
            .GET<Nothing>("/header").header("simple", "shouldReturnHeader")

        val response = conferenceClient
            .toBlocking()
            .exchange(request, String::class.java)

        Assertions.assertEquals("shouldReturnHeader", response.body())
    }

    @Test
    fun shouldReturnPathFromRawRequest() {
        val request = HttpRequest
            .GET<Nothing>("/rawRequest")

        val response = conferenceClient
            .toBlocking()
            .exchange(request, String::class.java)

        Assertions.assertEquals("/conferences/rawRequest", response.body())
    }
}

val ArbConference = arbitrary {
    val id = Arb.uuid().bind()
    val name = Arb.string(10..12).bind()
    val startAt = Arb.instant().bind()
    val updatedAt = Arb.instant().bind()
    val createdAt = Arb.instant().bind()
    Conference(
        id = id,
        name = name,
        startAt = startAt,
        updatedAt = updatedAt,
        createdAt = createdAt
    )
}

val ArbCreateConference = arbitrary {
    val name = Arb.string(10..12).bind()
    val startAt = Arb.instant(Instant.now(), Instant.now().plusSeconds(Duration.ofDays(365).toSeconds())).bind()
    CreateConference(
        name = name,
        startAt = startAt,
    )
}
