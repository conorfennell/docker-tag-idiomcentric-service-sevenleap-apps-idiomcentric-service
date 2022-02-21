package com.idiomcentric

import io.micronaut.runtime.Micronaut.build
import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Contact
import io.swagger.v3.oas.annotations.info.Info

@OpenAPIDefinition(
    info = Info(
        title = "idiomcentric service",
        version = "\${api.version}",
        description = "\${openapi.description}",
        contact = Contact(name = "Conor")
    )
)
object Application {
    @JvmStatic
    fun main(args: Array<String>) {
        build()
            .args(*args)
            .eagerInitSingletons(true)
            .eagerInitConfiguration(true)
            .packages("com.idiomcentric")
            .start()
    }
}
