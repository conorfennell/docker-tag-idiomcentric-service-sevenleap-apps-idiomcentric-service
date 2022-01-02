package com.idiomcentric.service.reddit

import io.micronaut.core.annotation.Introspected

@Introspected
data class RedditResponse(val data: RedditData)

@Introspected
data class RedditData(
    val children: List<RedditChild>
)

@Introspected
data class RedditChild(
    val data: RedditPost
)

@Introspected
data class RedditPost(
    val title: String,
    val url: String,
    val score: Int,
)