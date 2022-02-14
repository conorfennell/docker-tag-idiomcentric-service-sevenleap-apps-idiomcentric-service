package com.idiomcentric.dao.chunk

import com.idiomcentric.contollers.CreateChunk
import com.idiomcentric.dao.PostgresConnection
import jakarta.inject.Singleton
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import java.time.Instant
import java.util.UUID

@Singleton
class ChunkDao(private val connection: PostgresConnection) {

    suspend fun selectById(id: UUID): Chunk? = connection.query {
        ChunkTable.select {
            ChunkTable.id eq id
        }.firstOrNull()?.let(::mapToChunk)
    }

    suspend fun deleteById(id: UUID): Int = connection.query {
        ChunkTable.deleteWhere {
            ChunkTable.id eq id
        }
    }

    suspend fun insert(createChunk: CreateChunk): Chunk? = connection.query {
        ChunkTable.insert {
            it[id] = UUID.randomUUID()
            it[title] = createChunk.title
            it[body] = createChunk.body
            it[updatedAt] = Instant.now()
            it[createdAt] = Instant.now()
        }.resultedValues?.firstOrNull()?.let(::mapToChunk)
    }

    suspend fun update(updateChunk: Chunk): Int = connection.query {
        ChunkTable.update({ ChunkTable.id eq updateChunk.id }) {
            it[title] = updateChunk.title
            it[body] = updateChunk.body
            it[updatedAt] = Instant.now()
            it[createdAt] = updateChunk.createdAt
        }
    }

    suspend fun selectAll(): List<Chunk> = connection.query {
        ChunkTable.selectAll().map(::mapToChunk)
    }

    private fun mapToChunk(row: ResultRow): Chunk = Chunk(
        id = row[ChunkTable.id],
        title = row[ChunkTable.title],
        body = row[ChunkTable.body],
        updatedAt = row[ChunkTable.updatedAt],
        createdAt = row[ChunkTable.createdAt],
    )
}
