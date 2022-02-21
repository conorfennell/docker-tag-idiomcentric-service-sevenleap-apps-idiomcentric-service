package com.idiomcentric

import com.idiomcentric.contollers.ConferenceQuery
import com.idiomcentric.dao.conference.ConferenceDao
import com.idiomcentric.service.Deletion
import com.idiomcentric.service.Retrieval
import jakarta.inject.Singleton
import java.util.UUID

@Singleton
class ConferenceService(private val conferenceDao: ConferenceDao) {

    suspend fun all(): List<Conference> = conferenceDao.selectAll()

    suspend fun filter(query: ConferenceQuery): List<Conference> = conferenceDao.filter(query)

    suspend fun byId(id: UUID): Retrieval<Conference> = when (val conference = conferenceDao.selectById(id)) {
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
