package com.idiomcentric.service.reddit

import reactor.core.publisher.Flux

interface RedditClient {
    fun fetchTopPosts(): Flux<RedditPost>
}
