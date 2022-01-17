package com.idiomcentric

import com.idiomcentric.dao.conference.ConferenceDao
import jakarta.inject.Singleton
import java.time.Instant
import java.util.Random
import java.util.UUID

@Singleton
class ConferenceService(private val conferenceDao: ConferenceDao) {
    fun randomConference(): Conference = CONFERENCES[Random().nextInt(CONFERENCES.size)]

    companion object {
        private val CONFERENCES = listOf(
            Conference(UUID.randomUUID(), "Greach", Instant.now(), Instant.now()),
            Conference(UUID.randomUUID(), "GR8Conf EU", Instant.now(), Instant.now()),
            Conference(UUID.randomUUID(), "Micronaut Summit", Instant.now(), Instant.now()),
            Conference(UUID.randomUUID(), "Devoxx Belgium", Instant.now(), Instant.now()),
            Conference(UUID.randomUUID(), "Oracle Code One", Instant.now(), Instant.now()),
            Conference(UUID.randomUUID(), "CommitConf", Instant.now(), Instant.now()),
            Conference(UUID.randomUUID(), "Codemotion Madrid", Instant.now(), Instant.now())
        )
    }

    suspend fun all(): List<Conference> = conferenceDao.selectAll()

    suspend fun byId(id: UUID): Retrieval = when (val conference = conferenceDao.selectById(id)) {
        null -> Retrieval.NotFound
        else -> Retrieval.Retrieved(conference)
    }

    suspend fun deleteById(id: UUID): Deletion = when (conferenceDao.deleteById(id)) {
        0 -> Deletion.NotFound
        else -> Deletion.Deleted
    }

    suspend fun create(createConference: CreateConference): Conference? = conferenceDao.insert(createConference)

    suspend fun updateById(updateConference: Conference): Int = conferenceDao.update(updateConference)

    suspend fun partialUpdateById(updateConference: PatchConference): Int = conferenceDao.partialUpdate(updateConference)
}

sealed interface Deletion {
    object Deleted : Deletion
    object NotFound : Deletion
}

sealed interface Retrieval {
    data class Retrieved(val conference: Conference) : Retrieval
    object NotFound : Retrieval
}
