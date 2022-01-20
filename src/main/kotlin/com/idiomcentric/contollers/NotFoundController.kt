package com.idiomcentric.contollers

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Controller
import io.micronaut.http.hateoas.JsonError

@Controller("/notfound")
class NotFoundController {
    private val error: JsonError = JsonError("Page Not Found")

    @io.micronaut.http.annotation.Error(status = HttpStatus.NOT_FOUND, global = true)
    fun notFound(request: HttpRequest<*>): HttpResponse<JsonError> {
        val error: JsonError = JsonError("Page Not Found")

        return HttpResponse.notFound<JsonError>().body(error)
    }
}
