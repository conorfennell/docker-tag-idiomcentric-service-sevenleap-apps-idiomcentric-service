package com.idiomcentric

import org.flywaydb.core.Flyway
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.mockserver.client.MockServerClient
import org.testcontainers.containers.MockServerContainer
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.utility.DockerImageName


abstract class IntegrationProvider {
    companion object {
        val MOCKSERVER_IMAGE: DockerImageName = DockerImageName.parse("mockserver/mockserver:mockserver-5.11.2")
        val POSTGRES_SQL_IMAGE: DockerImageName = DockerImageName.parse("postgres:12.9")
    }

    val flyway: Flyway
    val mockServer: MockServerContainer = MockServerContainer(MOCKSERVER_IMAGE)
    val postgreSQLServer = PostgreSQLContainer(POSTGRES_SQL_IMAGE)
    val mockServerClient: MockServerClient

    init {
        mockServer.start()
        postgreSQLServer.start()
        mockServerClient = MockServerClient(mockServer.host, mockServer.serverPort);
        flyway = Flyway
            .configure()
            .dataSource(
                postgreSQLServer.jdbcUrl,
                postgreSQLServer.username,
                postgreSQLServer.password)
            .locations("db/migration", "db/callbacks").load()
    }

    fun loadResource(name: String): String = ClassLoader.getSystemResource(name).readText()

    @BeforeEach
    fun beforeEach() {
        flyway.migrate()
        mockServerClient.reset()
    }

    @AfterEach
    fun afterEach() {
        flyway.clean()
    }

    @AfterAll
    fun afterAll() {
        mockServer.stop()
        postgreSQLServer.stop()
    }
}
