package com.idiomcentric.service.reddit

import com.idiomcentric.logger
import com.idiomcentric.service.RedditPost
import com.idiomcentric.service.RedditResponse
import io.micronaut.cache.annotation.CacheConfig
import io.micronaut.cache.annotation.Cacheable
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.uri.UriBuilder
import io.micronaut.retry.annotation.Retryable
import jakarta.inject.Singleton
import java.net.URI

@Singleton
@CacheConfig("headlines")
open class RedditClient(
    @param:Client(RedditConfiguration.REDDIT_API_URL)
    private val httpClient: HttpClient,
    configuration: RedditConfiguration
) {

    private val uri: URI = UriBuilder.of(configuration.subReddit)
        .path(configuration.query)
        .queryParam("limit", configuration.limit)
        .queryParam("t", configuration.t)
        .build()

    @Retryable(attempts = "10", delay = "200ms")
    @Cacheable
    open fun fetchTopPosts(): List<RedditPost> {
        val request = HttpRequest.GET<RedditResponse>(uri)
        return httpClient.toBlocking()
            .retrieve(request, RedditResponse::class.java).data.children.map { it.data }

    }
}