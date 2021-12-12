package com.idiomcentric

import io.micronaut.core.annotation.Introspected
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import java.math.BigInteger
import java.security.SecureRandom

@Controller("/primes")
class PrimeController {
    private val random = SecureRandom()
    private val bitLength = 32;

    @Get("/random")
    fun randomPrime(): PrimeNumber = PrimeNumber(BigInteger.probablePrime(bitLength, random))

    @Get("/random/{num}")
    fun randomPrimes(num: Int): List<PrimeNumber> =
        List(num) { PrimeNumber(BigInteger.probablePrime(bitLength, random)) }
}

@Introspected
data class PrimeNumber(val prime: BigInteger)

