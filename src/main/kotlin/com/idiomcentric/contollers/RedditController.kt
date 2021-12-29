package com.idiomcentric.contollers

import com.idiomcentric.service.RedditPost
import com.idiomcentric.service.reddit.RedditClient
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/reddit")
open class RedditController(private val redditLowLevelClient: RedditClient) {

    @Get("/top")
    fun reddit(): List<RedditPost> = redditLowLevelClient.fetchTopPosts()
}