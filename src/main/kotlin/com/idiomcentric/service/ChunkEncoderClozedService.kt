package com.idiomcentric.service

import com.idiomcentric.contollers.CreateChunkEncoderClozed
import com.idiomcentric.dao.chunk.ChunkEncoderClozed
import com.idiomcentric.dao.chunk.ChunkEncoderClozedDao
import jakarta.inject.Singleton
import java.util.UUID

@Singleton
class ChunkEncoderClozedService(private val chunkEncoderClozedDao: ChunkEncoderClozedDao) {

    suspend fun all(): List<ChunkEncoderClozed> = chunkEncoderClozedDao.selectAll()

    suspend fun byChunkId(chunkId: UUID): List<ChunkEncoderClozed> = chunkEncoderClozedDao.selectAllByChunkId(chunkId)

    suspend fun byId(id: UUID): Retrieval<ChunkEncoderClozed> = when (val chunk = chunkEncoderClozedDao.selectById(id)) {
        null -> Retrieval.NotFound
        else -> Retrieval.Retrieved(chunk)
    }

    suspend fun deleteById(id: UUID): Deletion = when (chunkEncoderClozedDao.deleteById(id)) {
        0 -> Deletion.NotFound
        else -> Deletion.Deleted
    }

    suspend fun create(createChunk: CreateChunkEncoderClozed): Creation<ChunkEncoderClozed> = when (val chunk = chunkEncoderClozedDao.insert(createChunk)) {
        null -> Creation.Error
        else -> Creation.Retrieved(chunk)
    }

    suspend fun updateById(updateChunk: ChunkEncoderClozed): Update = when (chunkEncoderClozedDao.update(updateChunk)) {
        0 -> Update.NotFound
        else -> Update.Updated
    }
}
