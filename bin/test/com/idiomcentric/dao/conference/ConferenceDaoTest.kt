package com.idiomcentric.dao.conference

import com.idiomcentric.IntegrationProvider
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ConferenceDaoTest : IntegrationProvider() {

    @Inject
    lateinit var conferenceDao: ConferenceDao

    @Test
    fun shouldReturnAllConferences() {
        runBlocking {
            val conferences = conferenceDao.selectAll()
            Assertions.assertEquals(2, conferences.size, "should return all conferences")
        }
    }
}
