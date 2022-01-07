package com.idiomcentric

import jakarta.inject.Singleton
import java.util.Random

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
