package com.idiomcentric

import com.idiomcentric.service.reddit.RedditPost
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.mockserver.client.MockServerClient
import org.mockserver.model.HttpRequest.request
import org.mockserver.model.HttpResponse.response
import org.mockserver.model.MediaType

@MicronautTest(rebuildContext = true)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RedditControllerTest : TestPropertyProvider, IntegrationProvider() {

    @Inject
    @field:Client("/reddit")
    lateinit var redditClient: HttpClient

    @Test
    fun testRedditResponse() {
        // GET r/worldnews/top.json?limit=10&t=day
        MockServerClient(mockServer.host, mockServer.serverPort)
            .`when`(
                request()
                    .withPath("/r/worldnews/top.json")
                    .withQueryStringParameter("limit", "10")
                    .withQueryStringParameter("t", "day")
            )
            .respond(
                response()
                    .withBody(loadResponse("200_r_worldnews_top_json_ten.json"), MediaType.APPLICATION_JSON)
            )

        val actual: List<RedditPost> = redditClient
            .toBlocking()
            .retrieve(HttpRequest.GET<List<RedditPost>>("/top"), Argument.listOf(RedditPost::class.java))

        Assertions.assertEquals(3, actual.size, "should return three posts")
    }

    override fun getProperties(): MutableMap<String, String> = mutableMapOf(
        "reddit.host" to "http://${mockServer.host}",
        "reddit.port" to mockServer.serverPort.toString()
    )
}
