package com.idiomcentric.dao.chunk

import com.idiomcentric.contollers.CreateChunkEncoderClozed
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
class ChunkEncoderClozedDao(private val connection: PostgresConnection) {

    suspend fun selectAllByChunkId(chunkId: UUID): List<ChunkEncoderClozed> = connection.query {
        ChunkEncoderClozedTable.select {
            ChunkEncoderClozedTable.chunkId eq chunkId
        }.map(::mapToChunkEncoderClozed)
    }

    suspend fun selectById(id: UUID): ChunkEncoderClozed? = connection.query {
        ChunkEncoderClozedTable.select {
            ChunkEncoderClozedTable.id eq id
        }.firstOrNull()?.let(::mapToChunkEncoderClozed)
    }

    suspend fun deleteById(id: UUID): Int = connection.query {
        ChunkEncoderClozedTable.deleteWhere {
            ChunkEncoderClozedTable.id eq id
        }
    }

    suspend fun insert(create: CreateChunkEncoderClozed): ChunkEncoderClozed? = connection.query {
        ChunkEncoderClozedTable.insert {
            it[id] = UUID.randomUUID()
            it[sentence] = create.sentence
            it[clozedPositions] = create.clozedPositions.joinToString(",")
            it[updatedAt] = Instant.now()
            it[createdAt] = Instant.now()
        }.resultedValues?.firstOrNull()?.let(::mapToChunkEncoderClozed)
    }

    suspend fun update(update: ChunkEncoderClozed): Int = connection.query {
        ChunkEncoderClozedTable.update({ ChunkEncoderClozedTable.id eq update.id }) {
            it[sentence] = update.sentence
            it[clozedPositions] = update.clozedPositions.joinToString(",")
            it[updatedAt] = Instant.now()
            it[createdAt] = update.createdAt
        }
    }

    suspend fun selectAll(): List<ChunkEncoderClozed> = connection.query {
        ChunkEncoderClozedTable.selectAll().map(::mapToChunkEncoderClozed)
    }

    private fun mapToChunkEncoderClozed(row: ResultRow): ChunkEncoderClozed = ChunkEncoderClozed(
        id = row[ChunkEncoderClozedTable.id],
        chunkId = row[ChunkEncoderClozedTable.chunkId],
        sentence = row[ChunkEncoderClozedTable.sentence],
        clozedPositions = row[ChunkEncoderClozedTable.clozedPositions].split(",").map(String::toInt),
        updatedAt = row[ChunkEncoderClozedTable.updatedAt],
        createdAt = row[ChunkEncoderClozedTable.createdAt],
    )
}
