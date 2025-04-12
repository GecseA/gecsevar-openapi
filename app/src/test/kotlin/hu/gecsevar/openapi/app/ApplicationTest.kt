package hu.gecsevar.openapi.app

import io.ktor.server.testing.*
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertTrue

class SampleTest {


    @BeforeTest
    fun before() {

    }

    @Test
    fun testCamelCase() {
        assertTrue { true }
    }

    @Test
    fun testEndpoint1() = testApplication {
        environment {

        }
        application {

        }
    }
}