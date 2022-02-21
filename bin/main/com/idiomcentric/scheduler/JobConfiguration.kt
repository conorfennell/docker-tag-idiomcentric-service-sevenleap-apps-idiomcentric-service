package com.idiomcentric.scheduler

import io.micronaut.context.annotation.ConfigurationProperties
import javax.validation.constraints.NotNull

@ConfigurationProperties("jobs.reddit")
class JobConfiguration {

    @NotNull
    var fixedDelay: String? = null
}
