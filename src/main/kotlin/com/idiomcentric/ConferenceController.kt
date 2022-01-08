package com.idiomcentric

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule

@Controller("/conferences")
@Secured(SecurityRule.IS_ANONYMOUS)
class ConferenceController(private val conferenceService: ConferenceService) {

    @Get("/random")
    fun randomConference(): Conference = conferenceService.randomConference()
}
