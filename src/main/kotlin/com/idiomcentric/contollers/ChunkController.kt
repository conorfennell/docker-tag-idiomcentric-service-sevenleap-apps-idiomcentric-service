package com.idiomcentric.contollers

import com.idiomcentric.dao.chunk.Chunk
import com.idiomcentric.dao.chunk.ClozedFlashcard
import com.idiomcentric.service.ChunkService
import com.idiomcentric.service.ClozedFlashcardService
import com.idiomcentric.service.Creation
import com.idiomcentric.service.Deletion
import com.idiomcentric.service.Retrieval
import com.idiomcentric.service.Update
import io.micronaut.core.annotation.Introspected
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import mu.KLogger
import mu.KotlinLogging
import mu.withLoggingContext
import java.util.UUID

private val logger: KLogger = KotlinLogging.logger {}

@Controller("/api/chunks")
@Secured(SecurityRule.IS_ANONYMOUS)
class ChunkController(private val chunkService: ChunkService, private val clozedFlashcardService: ClozedFlashcardService) {

    @Get(processes = [MediaType.APPLICATION_JSON])
    suspend fun all(): List<Chunk> {
        return chunkService.all()
    }

    @Get("/{id}", headRoute = false)
    suspend fun byId(id: UUID): HttpResponse<Chunk?> = when (val retrieved = chunkService.byId(id)) {
        is Retrieval.NotFound -> HttpResponse.notFound()
        is Retrieval.Retrieved -> HttpResponse.ok(retrieved.value)
    }

    @Post(processes = [MediaType.APPLICATION_JSON])
    @Secured(SecurityRule.IS_ANONYMOUS)
    suspend fun create(@Body createChunk: CreateChunk): HttpResponse<Chunk> {
        withLoggingContext("ACTION" to "POST") {
            logger.info(createChunk.toString())
        }
        return when (val creation = chunkService.create(createChunk)) {
            is Creation.Error -> HttpResponse.serverError()
            is Creation.Retrieved -> HttpResponse.ok(creation.value)
        }
    }

    @Put("/{id}", processes = [MediaType.APPLICATION_JSON])
    suspend fun put(id: UUID, @Body updateChunk: Chunk): HttpResponse<Nothing> {
        withLoggingContext("ACTION" to "PUT", "CHUNK_ID" to id.toString()) {
            logger.info(updateChunk.toString())
        }

        return when (chunkService.updateById(updateChunk)) {
            is Update.NotFound -> HttpResponse.notFound()
            is Update.Updated -> HttpResponse.ok()
        }
    }

    @Delete("/{id}")
    suspend fun delete(id: UUID): HttpResponse<Nothing> {
        withLoggingContext("ACTION" to "DELETE", "CHUNK_ID" to id.toString()) {
            logger.info(id.toString())
        }

        return when (chunkService.deleteById(id)) {
            is Deletion.Deleted -> HttpResponse.ok()
            is Deletion.NotFound -> HttpResponse.notFound()
        }
    }

    @Get("/{chunkId}/flashcards/clozed", headRoute = false)
    suspend fun byChunkId(chunkId: UUID): List<ClozedFlashcard> = clozedFlashcardService.byChunkId(chunkId)

    @Get("/{chunkId}/flashcards/clozed/{id}", headRoute = false)
    suspend fun byChunkId(chunkId: UUID, id: UUID): HttpResponse<ClozedFlashcard?> = when (val retrieved = clozedFlashcardService.byId(id)) {
        is Retrieval.NotFound -> HttpResponse.notFound()
        is Retrieval.Retrieved -> HttpResponse.ok(retrieved.value)
    }

    @Post("/{chunkId}/flashcards/clozed", processes = [MediaType.APPLICATION_JSON])
    @Secured(SecurityRule.IS_ANONYMOUS)
    suspend fun create(@Body createClozedFlashcard: CreateClozedFlashcard): HttpResponse<ClozedFlashcard> {
        withLoggingContext(
            "ACTION" to "CREATE"
        ) { logger.info("Putting clozed") }

        return when (val creation = clozedFlashcardService.create(createClozedFlashcard)) {
            is Creation.Error -> HttpResponse.serverError()
            is Creation.Retrieved -> HttpResponse.ok(creation.value)
        }
    }

    @Put("/{chunkId}/flashcards/clozed/{id}", processes = [MediaType.APPLICATION_JSON])
    suspend fun put(chunkId: UUID, id: UUID, @Body updateClozedFlashcard: ClozedFlashcard): HttpResponse<Nothing> {
        withLoggingContext(
            "ACTION" to "UPDATE",
            "CHUNK_ID" to chunkId.toString(),
            "CLOZED" to id.toString()
        ) { logger.info("Putting clozed") }

        return when (clozedFlashcardService.updateById(updateClozedFlashcard)) {
            is Update.NotFound -> HttpResponse.notFound()
            is Update.Updated -> HttpResponse.ok()
        }
    }

    @Delete("/{chunkId}/flashcards/clozed/{id}")
    suspend fun delete(chunkId: UUID, id: UUID): HttpResponse<Nothing> {
        withLoggingContext(
            "ACTION" to "DELETE",
            "CHUNK_ID" to chunkId.toString(),
            "CLOZED" to id.toString()
        ) { logger.info("Deleting clozed") }

        return when (clozedFlashcardService.deleteById(id)) {
            is Deletion.Deleted -> {
                HttpResponse.noContent()
            }
            is Deletion.NotFound -> HttpResponse.notFound()
        }
    }
}

@Introspected
data class CreateChunk(
    val title: String,
    val body: String
)

@Introspected
data class CreateClozedFlashcard(
    val chunkId: UUID,
    val sentence: String,
    val clozedPositions: List<Int>,
)
