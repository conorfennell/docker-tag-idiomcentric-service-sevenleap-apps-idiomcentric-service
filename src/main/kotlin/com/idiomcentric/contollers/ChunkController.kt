package com.idiomcentric.contollers

import com.idiomcentric.dao.chunk.Chunk
import io.micronaut.core.annotation.Introspected
import io.micronaut.http.HttpResponse
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Put
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import java.time.Instant
import java.util.UUID

@Controller("/api/chunks")
@Secured(SecurityRule.IS_ANONYMOUS)
class ChunkController() {

    val chunkOne = Chunk(
        id = UUID.randomUUID(),
        title = "local storage",
        body = """
:::info
local storage only stores strings

:::

\
```javascript
localstorage.get("key")
```

\
""",
        createdAt = Instant.now(),
        updatedAt = Instant.now()
    )

    val chunkTwo = Chunk(
        id = UUID.randomUUID(),
        title = "fetch",
        body = """
:::tip
fetch is inbuilt now

:::

```javascript
fetch('http://localhost:8080/api/chunks')
```


---

<https://developer.mozilla.org/en-US/docs/Web/API/Fetch_API>

- [ ] add fetch question
- [ ] add multiple-choice questions

""",
        createdAt = Instant.now(),
        updatedAt = Instant.now()
    )

    val chunkThree = Chunk(
        id = UUID.randomUUID(),
        title = "Examle",
        body = """# First go

example list

- [ ] one
- [ ] two
- [ ] three
- [ ] four
- [ ] five
""",
        createdAt = Instant.now(),
        updatedAt = Instant.now()
    )

    val store = mutableMapOf<UUID, Chunk>(chunkOne.id to chunkOne, chunkTwo.id to chunkTwo, chunkThree.id to chunkThree)

    @Get(processes = [MediaType.APPLICATION_JSON])
    suspend fun all(): List<Chunk> {

        return store.values.toList()
    }

    @Post(processes = [MediaType.APPLICATION_JSON])
    suspend fun create(@Body createChunk: CreateChunk): Chunk {
        println(createChunk)

        val key = UUID.randomUUID()
        val chunk = Chunk(
            id = key,
            title = createChunk.title.ifEmpty { createChunk.body.takeWhile { it != '\n' } },
            body = createChunk.body,
            createdAt = Instant.now(),
            updatedAt = Instant.now()
        )
        store[key] = chunk

        return chunk
    }

    @Put(processes = [MediaType.APPLICATION_JSON])
    suspend fun put(@Body updateChunk: Chunk): HttpResponse<Nothing> {
        store[updateChunk.id] = updateChunk

        println(store[updateChunk.id])
        return HttpResponse.ok()
    }
}

@Introspected
data class CreateChunk(
    val title: String,
    val body: String
)
