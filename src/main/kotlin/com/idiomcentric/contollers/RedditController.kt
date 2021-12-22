package com.idiomcentric.contollers

import com.idiomcentric.service.RedditChildData
import com.idiomcentric.service.RedditLowLevelClient
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/reddit")
open class RedditController(private val redditLowLevelClient: RedditLowLevelClient) {

    @Get("/top")
    fun reddit(): List<RedditChildData> = redditLowLevelClient.fetchTopPosts()
}