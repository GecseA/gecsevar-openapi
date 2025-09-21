package hu.gecsevar.openapi.app

import hu.gecsevar.openapi.app.database.view.client.TestDto1
import hu.gecsevar.openapi.app.plugins.api.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.testing.*
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json
import org.junit.jupiter.api.Assertions.assertEquals
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class ApplicationTest {

    @OptIn(ExperimentalUuidApi::class)
    val testDto = TestDto1(
        id = 134243,
        myBool = true,
        myString = "myString",
        myStringId = Uuid.random(),
        myNumber = 234234,
        myNumber2 = 234235544,
        aDateTime = Clock.System.now(),
        aDate = Clock.System.now().toLocalDateTime(TimeZone.UTC).date,
        aTime = Clock.System.now().toLocalDateTime(TimeZone.UTC).time,
        myEnum = TestDto1.MyEnum.MY_ENUM_1,
        myOtherEnum = TestDto1.MyOtherEnum.MY_OTHER_ENUM_3,
        myArray = listOf(
            "string 1",
            "string 2",
            "string 3",
        ),
    )

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

        val response = getMyCrudEndpointWithId(
            client = client,
            url = Url(""),
            id = 134532,
        )

        assertTrue(response.status.isSuccess())
        val responseBody = Json.decodeFromString<TestDto1>(response.body())
        assertEquals(responseBody.id, 134532)
    }

    @Test
    fun `test My Crud Endpoint Id Put`() = testApplication {
        application {
            configureTestApplication()
        }

        val response = putMyCrudEndpointWithId(
            client = client.config {
                install(ContentNegotiation) {
                    json(Json { prettyPrint = true })
                }
            },
            url = Url(""),
            id = 100,
            testDto1 = testDto
        )

        assertEquals(response.status, HttpStatusCode.OK)
        val responseBody = Json.decodeFromString<TestDto1>(response.body())
        assertEquals(responseBody.id, 100)
    }

    @Test
    fun `test My Crud Endpoint Post`() = testApplication {
        application {
            configureTestApplication()
        }

        val response = postMyCrudEndpoint(
            client = client.config {
                install(ContentNegotiation) {
                    json(Json { prettyPrint = true })
                }
            },
            url = Url(""),
            testDto1 = testDto
        )

        assertTrue(response.status.isSuccess())
        val responseBody = Json.decodeFromString<TestDto1>(response.body())
        assertEquals(responseBody.id, 124710)
    }
}