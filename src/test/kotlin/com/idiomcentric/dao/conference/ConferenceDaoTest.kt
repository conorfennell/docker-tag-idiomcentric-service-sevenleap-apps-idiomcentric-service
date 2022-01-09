package com.idiomcentric.dao.conference

import com.idiomcentric.IntegrationProvider
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.micronaut.test.support.TestPropertyProvider
import jakarta.inject.Inject
import kotlinx.coroutines.runBlocking
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ConferenceDaoTest : TestPropertyProvider, IntegrationProvider() {

    @Inject
    lateinit var conferenceDao: ConferenceDao

    @Test
    fun shouldReturnAllConferences() {
        runBlocking {
            val conferences = conferenceDao.selectAll()
            Assertions.assertEquals(1, conferences.size, "should return all conferences")
        }
    }

    override fun getProperties(): MutableMap<String, String> = mutableMapOf(
        "datasources.default.url" to postgreSQLServer.jdbcUrl,
        "datasources.default.username" to postgreSQLServer.username,
        "datasources.default.password" to postgreSQLServer.password,
    )
}
