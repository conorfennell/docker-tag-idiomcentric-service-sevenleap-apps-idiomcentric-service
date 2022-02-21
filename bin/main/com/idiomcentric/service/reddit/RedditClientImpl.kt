package com.idiomcentric.service.reddit

import io.micronaut.cache.annotation.CacheConfig
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.uri.UriBuilder
import io.micronaut.retry.annotation.Retryable
import jakarta.inject.Singleton
import mu.KLogger
import mu.KotlinLogging
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.net.URI

val logger: KLogger = KotlinLogging.logger {}

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
    override fun fetchTopPosts(): Flux<RedditPost> {
        val request = HttpRequest.GET<RedditResponse>(uri)

        logger.info("querying reddit")
        return Mono.from(httpClient.retrieve(request, RedditResponse::class.java)).flatMapIterable { it.data.children.map { child -> child.data } }
    }
}
