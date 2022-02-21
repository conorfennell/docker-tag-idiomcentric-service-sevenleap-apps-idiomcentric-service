package com.idiomcentric.scheduler

import com.idiomcentric.service.reddit.RedditClient
import io.micronaut.scheduling.annotation.Scheduled
import jakarta.inject.Singleton
import mu.KLogger
import mu.KotlinLogging

val logger: KLogger = KotlinLogging.logger {}

@Singleton
open class RedditJob(private val redditClient: RedditClient) {

    @Scheduled(fixedDelay = "\${jobs.reddit.fixedDelay}")
    fun execute() {
        val posts = redditClient.fetchTopPosts()

        logger.info(posts.collectList().block().toString())
    }
}
