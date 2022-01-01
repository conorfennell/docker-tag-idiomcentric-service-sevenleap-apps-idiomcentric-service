package com.idiomcentric.service.reddit

import io.micronaut.context.annotation.ConfigurationProperties

@ConfigurationProperties("reddit")
open class RedditConfiguration {
    var host: String? = null
    var limit: Int? = null
    var port: Int? = null
    var query: String? = null
    var rPath: String? = null
    var subReddit: String? = null
    var t: String? = null
}
