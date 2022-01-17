package com.idiomcentric.dao.conference

import com.idiomcentric.Conference
import com.idiomcentric.CreateConference
import com.idiomcentric.PatchConference
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
class ConferenceDao(private val connection: PostgresConnection) {

    suspend fun selectById(id: UUID): Conference? = connection.query {
        ConferenceTable.select {
            ConferenceTable.id eq id
        }.firstOrNull()?.let(::mapToConference)
    }

    suspend fun deleteById(id: UUID): Int = connection.query {
        ConferenceTable.deleteWhere {
            ConferenceTable.id eq id
        }
    }

    suspend fun insert(createConference: CreateConference): Conference? = connection.query {
        ConferenceTable.insert {
            it[id] = UUID.randomUUID()
            it[name] = createConference.name
            it[updatedAt] = Instant.now()
            it[createdAt] = Instant.now()
        }.resultedValues?.firstOrNull()?.let(::mapToConference)
    }

    suspend fun update(updateConference: Conference): Int = connection.query {
        ConferenceTable.update({ ConferenceTable.id eq updateConference.id }) {
            it[name] = updateConference.name
            it[updatedAt] = Instant.now()
            it[createdAt] = updateConference.createdAt
        }
    }

    suspend fun partialUpdate(partialConference: PatchConference): Int = connection.query {
        ConferenceTable.update({ ConferenceTable.id eq partialConference.id }) {
            if (partialConference.name != null) {
                it[name] = partialConference.name
            }
            it[updatedAt] = Instant.now()
        }
    }

    suspend fun selectAll(): List<Conference> = connection.query {
        ConferenceTable.selectAll().map(::mapToConference)
    }

    private fun mapToConference(row: ResultRow): Conference = Conference(
        id = row[ConferenceTable.id],
        name = row[ConferenceTable.name],
        updatedAt = row[ConferenceTable.updatedAt],
        createdAt = row[ConferenceTable.createdAt],
    )
}
