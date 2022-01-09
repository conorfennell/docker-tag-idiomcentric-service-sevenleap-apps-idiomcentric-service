package com.idiomcentric.dao.conference

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.timestamp

object ConferenceTable : Table("CONFERENCE") {
    val id = uuid("ID")
    val name = varchar("NAME", length = 255)
    val createdAt = timestamp("CREATED_AT")
    val updatedAt = timestamp("UPDATED_AT")

    override val primaryKey = PrimaryKey(id)
}
