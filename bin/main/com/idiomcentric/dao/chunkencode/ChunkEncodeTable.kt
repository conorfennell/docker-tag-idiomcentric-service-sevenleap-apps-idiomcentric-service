package com.idiomcentric.dao.chunkencode

import com.idiomcentric.dao.chunk.ChunkTable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp
import java.time.Instant
import java.util.UUID

object ChunkEncodeTable : Table("CHUNK_ENCODE") {
    val id = uuid("ID")
    val chunkId = reference("CHUNK_ID", ChunkTable.id).uniqueIndex()
    val question = text("QUESTION")
    var answer = text("TEXT") // TODO what should collate be set to?
    val createdAt = timestamp("CREATED_AT")
    val updatedAt = timestamp("UPDATED_AT")

    override val primaryKey = PrimaryKey(id)
}

data class ChunkEncode(
    val id: UUID,
    val chunkId: UUID,
    val question: String,
    val answer: String,
    val createdAt: Instant,
    val updatedAt: Instant,
)
