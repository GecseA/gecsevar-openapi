package hu.gecsevar.openapi.app

import hu.gecsevar.openapi.app.database.view.TestDto1
import hu.gecsevar.openapi.app.plugins.api.client.getMyCrudEndpoint
import hu.gecsevar.openapi.app.plugins.api.client.myCrudEndpointIdDelete
import hu.gecsevar.openapi.app.plugins.api.client.myCrudEndpointIdGet
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test
import kotlin.test.assertTrue

class ApplicationTest {

    @Test
    fun `test Routing`() = testApplication {
        application {
            configureTestApplication()
        }
        client.get("/").let {
            assertEquals(HttpStatusCode.OK, it.status)
            assertEquals("Hello, world!", it.bodyAsText())
        }
    }

    @Test
    fun `test Get My Crud Endpoint`() = testApplication {
        application {
            configureTestApplication()
        }

        val response = getMyCrudEndpoint(
            client = client,
            url = Url(""),
            id = 124710,
            myString = "This is what I'll get"
        )

        assertTrue(response.status.isSuccess())
        val responseBody = Json.decodeFromString<List<TestDto1>>(response.body())
        assertTrue(responseBody.isNotEmpty())
        assertEquals(responseBody.first().id, 124710)
        assertEquals(responseBody.first().myString, "This is what I'll get")
    }

    @Test
    fun `test Delete My Crud Endpoint`() = testApplication {
        application {
            configureTestApplication()
        }

        val response = myCrudEndpointIdDelete(
            client = client,
            url = Url(""),
            id = 124710,
        )

        assertEquals(response.status, HttpStatusCode.NoContent)
    }

    @Test
    fun `test My Crud Endpoint Id Get`() = testApplication {
        application {
            configureTestApplication()
        }

        val response = myCrudEndpointIdGet(
            client = client,
            url = Url(""),
            id = 134532,
        )

        assertTrue(response.status.isSuccess())
        val responseBody = Json.decodeFromString<TestDto1>(response.body())
        assertEquals(responseBody.id, 134532)
    }
}