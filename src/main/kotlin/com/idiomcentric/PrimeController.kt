package com.idiomcentric

import io.micronaut.core.annotation.Introspected
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import mu.KLogger
import mu.KotlinLogging
import mu.withLoggingContext
import java.math.BigInteger
import java.security.SecureRandom

val logger: KLogger = KotlinLogging.logger {}
@Controller("/primes")
class PrimeController {
    private val random = SecureRandom()
    private val bitLength = 32;

    @Get("/random", produces = [MediaType.APPLICATION_JSON])
    suspend fun randomPrime(): PrimeNumber = PrimeNumber(BigInteger.probablePrime(bitLength, random))

    @Get("/random/{num}", produces = [MediaType.APPLICATION_JSON])
    suspend fun randomPrimes(num: Int): List<PrimeNumber> = runBlocking(Dispatchers.Default) {
        withLoggingContext("primes" to num.toString()) {
            logger.info("Generating prime numbers")
            List(num) { PrimeNumber(BigInteger.probablePrime(bitLength, random)) }
        }
    }
}

@Introspected
data class PrimeNumber(val prime: BigInteger)

