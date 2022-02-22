package com.idiomcentric.dao.chunk

import io.micronaut.core.annotation.Introspected
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant
import java.util.UUID

object ClozedFlashcardTable : Table("CLOZED_FLASHCARD") {
    val id = uuid("ID")
    val chunkId = reference("CHUNK_ID", ChunkTable.id)
    val sentence = text("SENTENCE")
    val clozedPositions = text("CLOZED_POSITIONS")
    val createdAt = timestamp("CREATED_AT")
    val updatedAt = timestamp("UPDATED_AT")
    override val primaryKey = PrimaryKey(id)
}

@Introspected
data class ClozedFlashcard(
    val id: UUID,
    val chunkId: UUID,
    val sentence: String,
    val clozedPositions: List<Int>,
    val createdAt: Instant,
    val updatedAt: Instant,
)
