package com.idiomcentric.contollers

import com.idiomcentric.Conference
import com.idiomcentric.ConferenceService
import com.idiomcentric.CreateConference
import com.idiomcentric.Deletion
import com.idiomcentric.PatchConference
import com.idiomcentric.Retrieval
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.CookieValue
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Head
import io.micronaut.http.annotation.Patch
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.http.annotation.Status
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import java.net.URI
import java.util.UUID

@Controller("/conferences")
@Secured(SecurityRule.IS_ANONYMOUS)
class ConferenceController(private val conferenceService: ConferenceService) {

    @Get
    suspend fun all(): List<Conference> = conferenceService.all()

    @Get("/{id}", headRoute = false)
    suspend fun byId(id: UUID): HttpResponse<Conference?> = when (val retrieval = conferenceService.byId(id)) {
        is Retrieval.NotFound -> HttpResponse.notFound()
        is Retrieval.Retrieved -> HttpResponse.ok(retrieval.conference)
    }

    @Delete("/{id}")
    suspend fun deleteById(id: UUID): HttpResponse<Nothing> = when (conferenceService.deleteById(id)) {
        Deletion.Deleted -> HttpResponse.ok<Nothing>().status(HttpStatus.NO_CONTENT)
        Deletion.NotFound -> HttpResponse.notFound()
    }

    @Post(processes = [MediaType.APPLICATION_JSON])
    suspend fun create(@Body createConference: CreateConference): HttpResponse<Conference?> = when (val conference = conferenceService.create(createConference)) {
        null -> HttpResponse.badRequest()
        else -> HttpResponse.created(conference, URI.create("/conferences/${conference.id}"))
    }

    @Put(processes = [MediaType.APPLICATION_JSON])
    suspend fun put(@Body updateConference: Conference): HttpResponse<Nothing> = when (conferenceService.updateById(updateConference)) {
        0 -> HttpResponse.notFound()
        else -> HttpResponse.ok<Nothing>().status(HttpStatus.NO_CONTENT)
    }

    @Patch(processes = [MediaType.APPLICATION_JSON])
    suspend fun patch(@Body patchConference: PatchConference): HttpResponse<Nothing> = when (conferenceService.partialUpdateById(patchConference)) {
        0 -> HttpResponse.notFound()
        else -> HttpResponse.ok<Nothing>().status(HttpStatus.NO_CONTENT)
    }

    @Head("/{id}")
    suspend fun headById(id: UUID): HttpResponse<Nothing> = when (conferenceService.byId(id)) {
        is Retrieval.NotFound -> HttpResponse.notFound()
        is Retrieval.Retrieved -> HttpResponse.ok()
    }

    @Status(HttpStatus.CONFLICT)
    @Get("/conflict")
    suspend fun conflict(): List<Conference> = conferenceService.all()

    @Get("/cookie")
    suspend fun cookie(@CookieValue("simple") cookies: String?): String? {
        return cookies
    }
}
