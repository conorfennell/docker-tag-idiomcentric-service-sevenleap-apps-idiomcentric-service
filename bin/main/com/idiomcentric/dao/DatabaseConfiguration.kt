package com.idiomcentric.dao

import io.micronaut.context.annotation.ConfigurationProperties
import javax.validation.constraints.NotNull

@ConfigurationProperties("datasources.default")
class DatabaseConfiguration {
    @NotNull
    var driverClassName: String? = null

    @NotNull
    var username: String? = null

    @NotNull
    var password: String? = null

    @NotNull
    var port: Int? = null

    @NotNull
    var hostname: String? = null

    @NotNull
    var database: String? = null

    @NotNull
    var url: String? = null

    @NotNull
    var maximumPoolSize: Int? = null

    @NotNull
    var enableMigration: Boolean? = null
}
