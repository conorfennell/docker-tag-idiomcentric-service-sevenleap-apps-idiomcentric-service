package com.idiomcentric.contollers

import com.idiomcentric.Conference
import com.idiomcentric.ConferenceService
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import java.util.UUID

@Controller("/conferences")
@Secured(SecurityRule.IS_ANONYMOUS)
class ConferenceController(private val conferenceService: ConferenceService) {

    @Get("/random")
    fun randomConference(): Conference = conferenceService.randomConference()

    @Get("/all")
    suspend fun all(): List<Conference> = conferenceService.all()

    @Get("/{id}")
    suspend fun byId(id: UUID): Conference? = conferenceService.byId(id)
}
