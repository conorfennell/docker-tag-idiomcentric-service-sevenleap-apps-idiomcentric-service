package com.idiomcentric.contollers

import com.idiomcentric.service.reddit.RedditClient
import com.idiomcentric.service.reddit.RedditPost
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.netty.cookies.NettyCookie
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule

@Controller("/reddit")
@Secured(SecurityRule.IS_ANONYMOUS)
open class RedditController(private val redditLowLevelClient: RedditClient) {

    @Get("/top")
    fun reddit(): List<RedditPost> = redditLowLevelClient.fetchTopPosts()

    @Get("/cookie")
    fun redditCookie(): HttpResponse<List<RedditPost>> {
        val response = HttpResponse.ok(redditLowLevelClient.fetchTopPosts())

        response.cookie(NettyCookie("simple", "simple").httpOnly(true))
        response.cookie(NettyCookie("change", "me"))

        return response
    }
}
