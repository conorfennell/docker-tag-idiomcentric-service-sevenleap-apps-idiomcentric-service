package com.idiomcentric.dao.chunk

import com.idiomcentric.contollers.CreateClozedFlashcard
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
class ClozedFlashcardDao(private val connection: PostgresConnection) {

    suspend fun selectAllByChunkId(chunkId: UUID): List<ClozedFlashcard> = connection.query {
        ClozedFlashcardTable.select {
            ClozedFlashcardTable.chunkId eq chunkId
        }.map(::mapToClozedFlashcard)
    }

    suspend fun selectById(id: UUID): ClozedFlashcard? = connection.query {
        ClozedFlashcardTable.select {
            ClozedFlashcardTable.id eq id
        }.firstOrNull()?.let(::mapToClozedFlashcard)
    }

    suspend fun deleteById(id: UUID): Int = connection.query {
        ClozedFlashcardTable.deleteWhere {
            ClozedFlashcardTable.id eq id
        }
    }

    suspend fun insert(create: CreateClozedFlashcard): ClozedFlashcard? = connection.query {
        ClozedFlashcardTable.insert {
            it[id] = UUID.randomUUID()
            it[chunkId] = create.chunkId
            it[sentence] = create.sentence
            it[clozedPositions] = create.clozedPositions.joinToString(",")
            it[updatedAt] = Instant.now()
            it[createdAt] = Instant.now()
        }.resultedValues?.firstOrNull()?.let(::mapToClozedFlashcard)
    }

    suspend fun update(update: ClozedFlashcard): Int = connection.query {
        ClozedFlashcardTable.update({ ClozedFlashcardTable.id eq update.id }) {
            it[chunkId] = update.chunkId
            it[sentence] = update.sentence
            it[clozedPositions] = update.clozedPositions.joinToString(",")
            it[updatedAt] = Instant.now()
            it[createdAt] = update.createdAt
        }
    }

    suspend fun selectAll(): List<ClozedFlashcard> = connection.query {
        ClozedFlashcardTable.selectAll().map(::mapToClozedFlashcard)
    }

    private fun mapToClozedFlashcard(row: ResultRow): ClozedFlashcard = ClozedFlashcard(
        id = row[ClozedFlashcardTable.id],
        chunkId = row[ClozedFlashcardTable.chunkId],
        sentence = row[ClozedFlashcardTable.sentence],
        clozedPositions = row[ClozedFlashcardTable.clozedPositions].split(",").map(String::toInt),
        updatedAt = row[ClozedFlashcardTable.updatedAt],
        createdAt = row[ClozedFlashcardTable.createdAt],
    )
}
