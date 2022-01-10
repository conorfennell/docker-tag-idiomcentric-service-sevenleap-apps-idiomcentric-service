package com.idiomcentric

import io.micronaut.core.annotation.Introspected
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import java.time.Instant

@Controller("/v1")
@Secured(SecurityRule.IS_ANONYMOUS)
class HealthController {
    private val start = Health()

    @Get("/health")
    suspend fun health(): Health = start
}

@Introspected
data class Health(val startupTime: Instant = Instant.now())
