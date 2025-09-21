package hu.gecsevar.openapi.app.api

import hu.gecsevar.openapi.app.database.view.TestDto1
import hu.gecsevar.openapi.app.plugins.api.TestCRUDApi
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class TestCRUDApiImpl: TestCRUDApi {

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun getMyCrudEndpoint(call: ApplicationCall) {
        // Request
        val id = call.request.queryParameters["id"]?.toInt()
        val myString = call.request.queryParameters["myString"]?.toString()
        // Response
        /**
         * code = 200, message = "A list of TestDto1 objects" response array: List<TestDto1>
         */
        call.respond(listOf(TestDto1(
            id = id ?: 0,
            myBool = true,
            myString = myString,
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
        )))
    }

    override suspend fun myCrudEndpointIdDelete(call: ApplicationCall) {
        // Request
        val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid id")
        // Response
        /**
         * code = 204, message = "TestDto1 deleted"
         * code = 404, message = "TestDto1 not found"
         */
        if (id == 124710)
            call.respond(HttpStatusCode.NoContent)
        else
            call.respond(HttpStatusCode.NotFound)
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun getMyCrudEndpointWithId(call: ApplicationCall) {
        // Request
        val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid id")
        // Response
        /**
         * code = 200, message = "A TestDto1 object", response: TestDto1
         * code = 404, message = "TestDto1 not found"
         */
        call.respond(TestDto1(
            id = id,
            myBool = true,
            myString = "Hello World!",
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
        ))
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun putMyCrudEndpointWithId(call: ApplicationCall) {
        // Request
        val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid id")
        val request = call.receive<TestDto1>()
        // Response
        /**
         * code = 200, message = "TestDto1 updated", response: TestDto1
         * code = 404, message = "TestDto1 not found"
         */
        call.respond(HttpStatusCode.OK,request.copy(id = id))
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun postMyCrudEndpoint(call: ApplicationCall) {
        // Request
        val request = call.receive<TestDto1>()
        // Response
        /**
         * code = 201, message = "TestDto1 created", response: TestDto1
         */
        call.respond(HttpStatusCode.Created,request.copy(id = 124710))
    }
}