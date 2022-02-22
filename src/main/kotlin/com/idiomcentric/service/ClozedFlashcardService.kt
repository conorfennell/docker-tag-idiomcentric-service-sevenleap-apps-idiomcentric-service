package com.idiomcentric.service

import com.idiomcentric.contollers.CreateClozedFlashcard
import com.idiomcentric.dao.chunk.ClozedFlashcard
import com.idiomcentric.dao.chunk.ClozedFlashcardDao
import jakarta.inject.Singleton
import java.util.UUID

@Singleton
class ClozedFlashcardService(private val clozedFlashcardDao: ClozedFlashcardDao) {

    suspend fun all(): List<ClozedFlashcard> = clozedFlashcardDao.selectAll()

    suspend fun byChunkId(chunkId: UUID): List<ClozedFlashcard> = clozedFlashcardDao.selectAllByChunkId(chunkId)

    suspend fun byId(id: UUID): Retrieval<ClozedFlashcard> = when (val chunk = clozedFlashcardDao.selectById(id)) {
        null -> Retrieval.NotFound
        else -> Retrieval.Retrieved(chunk)
    }

    suspend fun deleteById(id: UUID): Deletion = when (clozedFlashcardDao.deleteById(id)) {
        0 -> Deletion.NotFound
        else -> Deletion.Deleted
    }

    suspend fun create(createChunk: CreateClozedFlashcard): Creation<ClozedFlashcard> = when (val chunk = clozedFlashcardDao.insert(createChunk)) {
        null -> Creation.Error
        else -> Creation.Retrieved(chunk)
    }

    suspend fun updateById(updateChunk: ClozedFlashcard): Update = when (clozedFlashcardDao.update(updateChunk)) {
        0 -> Update.NotFound
        else -> Update.Updated
    }
}
