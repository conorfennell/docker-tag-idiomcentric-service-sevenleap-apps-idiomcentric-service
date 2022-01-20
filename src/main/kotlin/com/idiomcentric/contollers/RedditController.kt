package com.idiomcentric.contollers

import com.idiomcentric.service.reddit.RedditClient
import com.idiomcentric.service.reddit.RedditPost
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule

@Controller("/reddit")
@Secured(SecurityRule.IS_ANONYMOUS)
open class RedditController(private val redditLowLevelClient: RedditClient) {

    @Get("/top")
    fun reddit(): List<RedditPost> = redditLowLevelClient.fetchTopPosts()

    @Get("/blah")
    fun blah(): HttpResponse<Nothing> = HttpResponse.notFound<Nothing>()
}
