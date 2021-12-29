package com.idiomcentric.service.reddit

import io.micronaut.context.annotation.ConfigurationProperties
import io.micronaut.context.annotation.Requires

@ConfigurationProperties(RedditConfiguration.PREFIX)
@Requires(property = RedditConfiguration.PREFIX)
class RedditConfiguration {
    var t: String = "day"
    var limit: Int = 10
    var subReddit: String = "worldnews"
    var query = "top.json"

    companion object {
        const val PREFIX = "reddit"
        const val REDDIT_API_URL = "https://www.reddit.com/r"
    }
}