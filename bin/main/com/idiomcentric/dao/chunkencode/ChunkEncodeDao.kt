package com.idiomcentric.dao.chunkencode

import com.idiomcentric.dao.PostgresConnection
import jakarta.inject.Singleton
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update
import java.time.Instant
import java.util.UUID

@Singleton
class ChunkEncodeDao(private val connection: PostgresConnection) {

    suspend fun selectById(id: UUID): ChunkEncode? = connection.query {
        ChunkEncodeTable.select {
            ChunkEncodeTable.id eq id
        }.firstOrNull()?.let(::mapToChunkEncode)
    }

    suspend fun deleteById(id: UUID): Int = connection.query {
        ChunkEncodeTable.deleteWhere {
            ChunkEncodeTable.id eq id
        }
    }

    suspend fun update(updateChunkEncode: ChunkEncode): Int = connection.query {
        ChunkEncodeTable.update({ ChunkEncodeTable.id eq updateChunkEncode.id }) {
            it[chunkId] = updateChunkEncode.chunkId
            it[question] = updateChunkEncode.question
            it[answer] = updateChunkEncode.answer
            it[updatedAt] = Instant.now()
            it[createdAt] = updateChunkEncode.createdAt
        }
    }

    suspend fun selectAll(): List<ChunkEncode> = connection.query {
        ChunkEncodeTable.selectAll().map(::mapToChunkEncode)
    }

    private fun mapToChunkEncode(row: ResultRow): ChunkEncode = ChunkEncode(
        id = row[ChunkEncodeTable.id],
        chunkId = row[ChunkEncodeTable.chunkId],
        question = row[ChunkEncodeTable.question],
        answer = row[ChunkEncodeTable.answer],
        updatedAt = row[ChunkEncodeTable.updatedAt],
        createdAt = row[ChunkEncodeTable.createdAt],
    )
}
