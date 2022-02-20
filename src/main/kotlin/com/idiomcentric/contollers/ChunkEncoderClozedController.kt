package com.idiomcentric.contollers

import com.idiomcentric.dao.chunk.ChunkEncoderClozed
import com.idiomcentric.service.ChunkEncoderClozedService
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
class ChunkEncoderClozedController(private val chunkEncoderClozedService: ChunkEncoderClozedService) {

    @Get("/{chunkId}/chunkencoderclozed", headRoute = false)
    suspend fun byChunkId(chunkId: UUID): List<ChunkEncoderClozed> = chunkEncoderClozedService.byChunkId(chunkId)

    @Get("/{chunkId}/chunkencoderclozed/{id}", headRoute = false)
    suspend fun byChunkId(chunkId: UUID, id: UUID): HttpResponse<ChunkEncoderClozed?> = when (val retrieved = chunkEncoderClozedService.byId(id)) {
        is Retrieval.NotFound -> HttpResponse.notFound()
        is Retrieval.Retrieved -> HttpResponse.ok(retrieved.value)
    }

    @Post(processes = [MediaType.APPLICATION_JSON])
    @Secured(SecurityRule.IS_ANONYMOUS)
    suspend fun create(@Body createChunk: CreateChunkEncoderClozed): HttpResponse<ChunkEncoderClozed> {
        withLoggingContext("ACTION" to "POST") {
            logger.info(createChunk.toString())
        }
        return when (val creation = chunkEncoderClozedService.create(createChunk)) {
            is Creation.Error -> HttpResponse.serverError()
            is Creation.Retrieved -> HttpResponse.ok(creation.value)
        }
    }

    @Put("/{id}", processes = [MediaType.APPLICATION_JSON])
    suspend fun put(id: UUID, @Body updateChunkEncoder: ChunkEncoderClozed): HttpResponse<Nothing> {
        withLoggingContext("ACTION" to "PUT", "CHUNK_ENCODER_ID" to id.toString()) {
            logger.info(updateChunkEncoder.toString())
        }

        return when (chunkEncoderClozedService.updateById(updateChunkEncoder)) {
            is Update.NotFound -> HttpResponse.notFound()
            is Update.Updated -> HttpResponse.ok()
        }
    }

    @Delete("/{id}")
    suspend fun delete(id: UUID): HttpResponse<Nothing> {
        withLoggingContext("ACTION" to "DELETE", "CHUNK_ID" to id.toString()) {
            logger.info(id.toString())
        }

        return when (chunkEncoderClozedService.deleteById(id)) {
            is Deletion.Deleted -> HttpResponse.ok()
            is Deletion.NotFound -> HttpResponse.notFound()
        }
    }
}

@Introspected
data class CreateChunkEncoderClozed(
    val chunkId: UUID,
    val sentence: String,
    val clozedPositions: List<Int>,
)
