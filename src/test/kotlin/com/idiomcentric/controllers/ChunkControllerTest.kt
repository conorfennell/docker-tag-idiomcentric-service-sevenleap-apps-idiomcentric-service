package com.idiomcentric.controllers

import com.idiomcentric.IntegrationProvider
import com.idiomcentric.dao.chunk.Chunk
import io.kotest.property.Arb
import io.kotest.property.arbitrary.arbitrary
import io.kotest.property.arbitrary.instant
import io.kotest.property.arbitrary.next
import io.kotest.property.arbitrary.string
import io.kotest.property.arbitrary.uuid
import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import java.util.UUID

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ChunkControllerTest : IntegrationProvider() {

    @Inject
    @field:Client("/api/chunks")
    lateinit var chunkClient: HttpClient

    @Test
    fun shouldReturnNoChunksSuccessfully() {
        val actual: List<Chunk> = chunkClient
            .toBlocking()
            .retrieve(HttpRequest.GET<List<Chunk>>(""), Argument.listOf(Chunk::class.java))

        Assertions.assertEquals(0, actual.size, "should return 0 chunks")
    }

    @Test
    fun shouldReturnNoChunkSuccessfully() {
        val chunkId = UUID.randomUUID()
        val thrown = Assertions.assertThrows(
            HttpClientResponseException::class.java,
            {
                chunkClient
                    .toBlocking()
                    .retrieve(HttpRequest.GET<List<Chunk>>("/$chunkId"), Argument.listOf(Chunk::class.java))
            },
            "HttpClientResponseException Not Found expected"
        )

        Assertions.assertEquals("Not Found", thrown.message)
    }

    @Test
    fun shouldCreateChunk() {
        val createChunk = ArbCreateChunk.next()

        val chunk = chunkClient
            .toBlocking()
            .retrieve(HttpRequest.POST("", createChunk), Chunk::class.java)

        val actual: Chunk = chunkClient
            .toBlocking()
            .retrieve(HttpRequest.GET<Chunk>("/${chunk.id}"), Chunk::class.java)

        Assertions.assertEquals(chunk, actual, "chunks are the same")
    }

    @Test
    fun shouldCreateDeleteChunk() {
        val createChunk = ArbCreateChunk.next()

        val chunk = chunkClient
            .toBlocking()
            .retrieve(HttpRequest.POST("", createChunk), Chunk::class.java)

        chunkClient
            .toBlocking()
            .exchange<Nothing, Nothing>(HttpRequest.DELETE("/${chunk.id}"))

        val chunks: List<Chunk> = chunkClient
            .toBlocking()
            .retrieve(HttpRequest.GET<List<Chunk>>(""), Argument.listOf(Chunk::class.java))

        Assertions.assertEquals(0, chunks.size, "should return 0 chunks")
    }

    @Test
    fun shouldReturnNotFoundOnDeleteChunk() {
        val missingId = UUID.randomUUID()

        Assertions.assertThrows(HttpClientResponseException::class.java, {
            chunkClient
                .toBlocking()
                .exchange<Nothing, Nothing>(HttpRequest.DELETE("/$missingId"))
        }, "HttpClientResponseException Not Found expected")
    }

    @Test
    fun shouldReturnNotFoundOnUpdateChunk() {
        val chunk = ArbChunk.next()

        Assertions.assertThrows(HttpClientResponseException::class.java, {
            chunkClient
                .toBlocking()
                .exchange<Chunk, Nothing>(HttpRequest.PUT("/${chunk.id}", chunk))
        }, "HttpClientResponseException Not Found expected")
    }

    @Test
    fun shouldCreateUpdateChunk() {
        val createChunk = ArbCreateChunk.next()

        val chunk = chunkClient
            .toBlocking()
            .retrieve(HttpRequest.POST("", createChunk), Chunk::class.java)

        val update = chunk.copy(title = "update")
        chunkClient
            .toBlocking()
            .exchange<Chunk, Nothing>(HttpRequest.PUT("/${chunk.id}", update))

        val actual: Chunk = chunkClient
            .toBlocking()
            .retrieve(HttpRequest.GET<Chunk>("/${chunk.id}"), Chunk::class.java)

        Assertions.assertEquals(update.title, actual.title, "returned chunk should be updated")
    }
}

val ArbChunk = arbitrary {
    val id = Arb.uuid().bind()
    val title = Arb.string(10..12).bind()
    val body = Arb.string(10..12).bind()
    val updatedAt = Arb.instant().bind()
    val createdAt = Arb.instant().bind()
    Chunk(
        id = id,
        title = title,
        body = body,
        updatedAt = updatedAt,
        createdAt = createdAt
    )
}
