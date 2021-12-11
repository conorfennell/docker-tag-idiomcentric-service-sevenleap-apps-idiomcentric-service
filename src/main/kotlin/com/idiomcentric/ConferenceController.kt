package com.idiomcentric

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get

@Controller("/conferences")
class ConferenceController(private val conferenceService: ConferenceService) {

    @Get("/random")
    fun randomConference(): Conference = conferenceService.randomConference()
}