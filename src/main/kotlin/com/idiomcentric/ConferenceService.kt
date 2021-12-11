package com.idiomcentric

import java.util.Random
import jakarta.inject.Singleton

@Singleton
class ConferenceService {
    fun randomConference(): Conference = CONFERENCES[Random().nextInt(CONFERENCES.size)]

    companion object {
        private val CONFERENCES = listOf(
            Conference("Greach"),
            Conference("GR8Conf EU"),
            Conference("Micronaut Summit"),
            Conference("Devoxx Belgium"),
            Conference("Oracle Code One"),
            Conference("CommitConf"),
            Conference("Codemotion Madrid")
        )
    }

}