package com.idiomcentric.service.reddit

interface RedditClient {
    fun fetchTopPosts(): List<RedditPost>
}
