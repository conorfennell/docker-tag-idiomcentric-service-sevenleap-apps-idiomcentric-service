package com.idiomcentric.service.reddit

import io.micronaut.cache.annotation.CacheConfig
import io.micronaut.cache.annotation.Cacheable
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.uri.UriBuilder
import io.micronaut.retry.annotation.Retryable
import jakarta.inject.Singleton
import java.net.URI

@Singleton
@CacheConfig("headlines")
open class RedditClientImpl(
    private val httpClient: HttpClient,
    configuration: RedditConfiguration
) : RedditClient {

    private val uri: URI = UriBuilder
        .of("${configuration.host}:${configuration.port}")
        .path(configuration.rPath)
        .path(configuration.subReddit)
        .path(configuration.query)
        .queryParam("limit", configuration.limit)
        .queryParam("t", configuration.t)
        .build()

    @Retryable(attempts = "10", delay = "200ms")
    @Cacheable
    override fun fetchTopPosts(): List<RedditPost> {
        val request = HttpRequest.GET<RedditResponse>(uri)
        return httpClient.toBlocking()
            .retrieve(request, RedditResponse::class.java).data.children.map { it.data }
    }
}
