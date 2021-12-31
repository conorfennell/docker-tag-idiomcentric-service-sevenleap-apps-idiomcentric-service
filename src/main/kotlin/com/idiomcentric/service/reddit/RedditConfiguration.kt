package com.idiomcentric.service.reddit

import io.micronaut.context.annotation.ConfigurationProperties
import io.micronaut.context.annotation.Requires

@ConfigurationProperties(RedditConfiguration.PREFIX)
@Requires(property = RedditConfiguration.PREFIX)
class RedditConfiguration {
    var host: String = "https://www.reddit.com"
    var limit: Int = 10
    var port = 443
    var query = "top.json"
    var rPath: String = "r"
    var subReddit: String = "worldnews"
    var t: String = "day"

    companion object {
        const val PREFIX = "reddit"
    }
}