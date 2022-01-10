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
}
