package com.idiomcentric.contollers

import com.idiomcentric.Conference
import com.idiomcentric.ConferenceService
import com.idiomcentric.CreateConference
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import java.net.URI
import java.util.UUID

@Controller("/conferences")
@Secured(SecurityRule.IS_ANONYMOUS)
class ConferenceController(private val conferenceService: ConferenceService) {

    @Get("/random")
    fun randomConference(): Conference = conferenceService.randomConference()

    @Get("/all")
    suspend fun all(): List<Conference> = conferenceService.all()

    @Get("/{id}")
    suspend fun byId(id: UUID): HttpResponse<Conference?> = when (val conference = conferenceService.byId(id)) {
        null -> HttpResponse.notFound()
        else -> HttpResponse.ok(conference)
    }

    @Delete("/{id}")
    suspend fun deleteById(id: UUID): HttpResponse<Conference?> = when (conferenceService.deleteById(id)) {
        0 -> HttpResponse.notFound()
        else -> HttpResponse.ok<Conference?>().status(HttpStatus.NO_CONTENT)
    }

    @Post("/create", processes = [MediaType.APPLICATION_JSON])
    suspend fun create(@Body createConference: CreateConference): HttpResponse<Conference?> = when (val conference = conferenceService.create(createConference)) {
        null -> HttpResponse.badRequest()
        else -> HttpResponse.created(conference, URI.create("/conferences/${conference.id}"))
    }
}
