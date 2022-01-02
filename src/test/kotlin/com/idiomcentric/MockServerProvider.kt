package com.idiomcentric

import org.junit.jupiter.api.AfterAll
import org.testcontainers.containers.MockServerContainer
import org.testcontainers.utility.DockerImageName

abstract class MockServerProvider {
    companion object {
        val MOCKSERVER_IMAGE: DockerImageName = DockerImageName.parse("mockserver/mockserver:mockserver-5.11.2")
    }

    val mockServer: MockServerContainer = MockServerContainer(MOCKSERVER_IMAGE)

    init {
        mockServer.start()
    }

    fun loadResponse(name: String): String = ClassLoader.getSystemResource(name).readText()

    @AfterAll
    fun afterAll() {
        mockServer.stop()
    }
}
