package com.idiomcentric

import io.micronaut.core.annotation.Introspected
import java.time.Instant
import java.util.UUID

@Introspected
data class Conference(
    val id: UUID,
    val name: String,
    val createdAt: Instant,
    val updatedAt: Instant
)

@Introspected
data class PatchConference(
    val id: UUID,
    val name: String?,
)

@Introspected
data class CreateConference(
    val name: String
)
