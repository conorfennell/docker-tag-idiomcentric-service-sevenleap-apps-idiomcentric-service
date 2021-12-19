package com.idiomcentric.contollers

import com.idiomcentric.service.RedditChildData
import com.idiomcentric.service.RedditLowLevelClient
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import kotlinx.coroutines.FlowPreview

@Controller("/reddit")
class RedditController(private val redditLowLevelClient: RedditLowLevelClient) {

    @Get("/top")
    @OptIn(FlowPreview::class)
    fun reddit(): List<RedditChildData> = redditLowLevelClient.fetchTopPosts()
}