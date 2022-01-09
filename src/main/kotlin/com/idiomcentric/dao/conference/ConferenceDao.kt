package com.idiomcentric.dao.conference

import com.idiomcentric.Conference
import com.idiomcentric.dao.PostgresConnection
import jakarta.inject.Singleton
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.select
import java.util.UUID

@Singleton
class ConferenceDao(private val connection: PostgresConnection) {

    suspend fun selectById(id: UUID): Conference? = connection.query {
        ConferenceTable.select {
            ConferenceTable.id eq id
        }.firstOrNull()?.let(::mapToConference)
    }

    private fun mapToConference(row: ResultRow) =
        Conference(
            id = row[ConferenceTable.id],
            name = row[ConferenceTable.name],
            updatedAt = row[ConferenceTable.updatedAt],
            createdAt = row[ConferenceTable.createdAt],
        )
}
