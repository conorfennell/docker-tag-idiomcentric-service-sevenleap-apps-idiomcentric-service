package com.idiomcentric

import io.micronaut.core.annotation.Introspected
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import java.time.Instant

@Controller("/")
class HealthController(private val conferenceService: ConferenceService) {
    private val start = Health()

    @Get("/health")
    fun health(): Health = start
}

@Introspected
data class Health(val startupTime: Instant = Instant.now())
