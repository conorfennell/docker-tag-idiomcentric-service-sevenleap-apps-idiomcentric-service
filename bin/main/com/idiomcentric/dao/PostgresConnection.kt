package com.idiomcentric.dao

import jakarta.inject.Singleton
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.Transaction
import org.jetbrains.exposed.sql.transactions.transaction
import javax.sql.DataSource

@Singleton
class PostgresConnection(private val hikariDataSource: DataSource, databaseConfiguration: DatabaseConfiguration) {
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

    private fun hikari(databaseConfiguration: DatabaseConfiguration): DataSource {
        if (databaseConfiguration.enableMigration == true) {
            val flyway = Flyway
                .configure()
                .dataSource(
                    databaseConfiguration.url,
                    databaseConfiguration.username,
                    databaseConfiguration.password
                )
                .locations("db/migration").load()
            flyway.migrate()
        }

        return hikariDataSource
    }
}
