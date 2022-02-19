package com.idiomcentric.service

import com.idiomcentric.contollers.CreateChunk
import com.idiomcentric.dao.chunk.Chunk
import com.idiomcentric.dao.chunk.ChunkDao
import jakarta.inject.Singleton
import java.util.UUID

@Singleton
class ChunkService(private val chunkDao: ChunkDao) {

    suspend fun all(): List<Chunk> = chunkDao.selectAll()

    suspend fun byId(id: UUID): Retrieval<Chunk> = when (val chunk = chunkDao.selectById(id)) {
        null -> Retrieval.NotFound
        else -> Retrieval.Retrieved(chunk)
    }

    suspend fun deleteById(id: UUID): Deletion = when (chunkDao.deleteById(id)) {
        0 -> Deletion.NotFound
        else -> Deletion.Deleted
    }

    suspend fun create(createChunk: CreateChunk): Creation<Chunk> = when (val chunk = chunkDao.insert(createChunk)) {
        null -> Creation.Error
        else -> Creation.Retrieved(chunk)
    }

    suspend fun updateById(updateChunk: Chunk): Update = when (chunkDao.update(updateChunk)) {
        0 -> Update.NotFound
        else -> Update.Updated
    }
}
