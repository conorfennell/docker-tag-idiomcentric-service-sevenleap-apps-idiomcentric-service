package com.idiomcentric.contollers

import com.idiomcentric.dao.chunk.Chunk
import com.idiomcentric.dao.chunk.ChunkDao
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
class ChunkController(private val chunkDao: ChunkDao) {

    @Get(processes = [MediaType.APPLICATION_JSON])
    suspend fun all(): List<Chunk> {
        return chunkDao.selectAll()
    }

    @Post(processes = [MediaType.APPLICATION_JSON])
    suspend fun create(@Body createChunk: CreateChunk): Chunk {
        withLoggingContext("ACTION" to "POST") {
            logger.info(createChunk.toString())
        }
        return chunkDao.insert(createChunk)!!
    }

    @Put("/{id}", processes = [MediaType.APPLICATION_JSON])
    suspend fun put(id: UUID, @Body updateChunk: Chunk): HttpResponse<Nothing> {
        withLoggingContext("ACTION" to "PUT", "CHUNK_ID" to id.toString()) {
            logger.info(updateChunk.toString())
        }

        chunkDao.update(updateChunk)
        return HttpResponse.ok()
    }

    @Delete("/{id}")
    suspend fun delete(id: UUID): HttpResponse<Nothing> {
        withLoggingContext("ACTION" to "DELETE", "CHUNK_ID" to id.toString()) {
            logger.info(id.toString())
        }

        chunkDao.deleteById(id)
        return HttpResponse.ok()
    }
}

@Introspected
data class CreateChunk(
    val title: String,
    val body: String
)
