package com.idiomcentric.dao

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import jakarta.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.transaction

@Singleton
class PostgresConnection(databaseConfiguration: DatabaseConfiguration) {
    private val database: Database

    init {
        database = Database.connect(hikari(databaseConfiguration))
    }

    suspend fun <T> query(block: (Transaction) -> T): T =
        withContext(Dispatchers.IO) {
            transaction(db = database) {
                block(this)
            }
        }

    fun hikari(databaseConfiguration: DatabaseConfiguration): HikariDataSource {
        val hikariConfig = HikariConfig()

        with(hikariConfig) {
            jdbcUrl = databaseConfiguration.url
            driverClassName = databaseConfiguration.driverClassName
            username = databaseConfiguration.username
            password = databaseConfiguration.password
            maximumPoolSize = databaseConfiguration.maxPoolSize ?: 10

            validate()
        }

        return HikariDataSource(hikariConfig)
    }
}
