package com.idiomcentric.service.reddit

import io.micronaut.context.annotation.ConfigurationProperties
import javax.validation.constraints.NotNull

@ConfigurationProperties("reddit")
open class RedditConfiguration {
    @NotNull
    var host: String? = null

    @NotNull
    var limit: Int? = null

    @NotNull
    var port: Int? = null

    @NotNull
    var query: String? = null

    @NotNull
    var rPath: String? = null

    @NotNull
    var subReddit: String? = null

    @NotNull
    var t: String? = null
}
