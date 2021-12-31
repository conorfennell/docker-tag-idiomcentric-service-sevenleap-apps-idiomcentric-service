package com.idiomcentric.service.reddit

import com.idiomcentric.service.RedditPost

interface RedditClient {
    fun fetchTopPosts(): List<RedditPost>
}
