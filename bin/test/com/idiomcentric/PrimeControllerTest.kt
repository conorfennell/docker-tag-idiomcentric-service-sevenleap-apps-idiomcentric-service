package com.idiomcentric

import io.micronaut.core.type.Argument
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance

@MicronautTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PrimeControllerTest : IntegrationProvider() {

    @Inject
    @field:Client("/primes")
    lateinit var primesClient: HttpClient

    @Test
    fun testPrimeRandom() {
        val actual: PrimeNumber =
            primesClient.toBlocking().retrieve(HttpRequest.GET<PrimeNumber>("/random"), PrimeNumber::class.java)

        Assertions.assertNotNull(actual, "prime number should not be null")
    }

    @Test
    fun testPrimeRandomTen() {
        val actual: List<PrimeNumber> = primesClient.toBlocking()
            .retrieve(HttpRequest.GET<List<PrimeNumber>>("/random/10"), Argument.listOf(PrimeNumber::class.java))

        Assertions.assertNotNull(actual, "prime number should not be null")
        Assertions.assertEquals(10, actual.size, "should return ten primes")
    }
}
