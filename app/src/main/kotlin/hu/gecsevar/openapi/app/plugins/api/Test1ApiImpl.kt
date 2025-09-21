package hu.gecsevar.openapi.app.plugins.api

import hu.gecsevar.openapi.app.database.view.TestDto1
import hu.gecsevar.openapi.app.database.view.TestDto2
import hu.gecsevar.openapi.app.database.view.TestDto3
import io.ktor.server.application.*
import io.ktor.server.response.*
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

class Test1ApiImpl: Test1Api {
    override suspend fun getDynamicResources(call: ApplicationCall) {
        TODO("Not yet implemented")
    }

    @OptIn(ExperimentalUuidApi::class)
    override suspend fun getFirstTestsByIdAndName(call: ApplicationCall) {
        // Request
        // val resourceStringId = call.request.queryParameters["resourceStringId"]?.toString()
        // Response
        /**
         * code = 200, message = "OK", response: TestDto1
         */
        val resourceStringId = call.request.queryParameters["resourceStringId"]

        call.respond(TestDto1(
            id = 4323424,
            myBool = true,
            myString = resourceStringId,
            myStringId = Uuid.random(),
            myNumber = 324,
            myNumber2 = 5435,
            aDateTime = Clock.System.now(),
            aDate = Clock.System.now().toLocalDateTime(TimeZone.UTC).date,
            aTime = Clock.System.now().toLocalDateTime(TimeZone.UTC).time,
            myEnum = TestDto1.MyEnum.MY_ENUM_2,
            myOtherEnum = TestDto1.MyOtherEnum.MY_OTHER_ENUM_2,
            myArray = emptyList(),
        ))
    }

    override suspend fun getSecondTestsByIdAndName(call: ApplicationCall) {
        // Request
        // val id = call.parameters["id"].toInt() ?: throw IllegalArgumentException("Invalid id")
        // val name = call.parameters["name"].toString() ?: throw IllegalArgumentException("Invalid name")
        // Response
        /**
         * code = 200, message = "OK", response: TestDto2
         */
        val id = call.parameters["id"]?.toInt() ?: throw IllegalArgumentException("Invalid id")
        val name = call.parameters["name"].toString() ?: throw IllegalArgumentException("Invalid name")

        call.respond(TestDto2(
            id = TODO(),
            myChildUnderscore1 = TODO(),
            myChildsUnderscore1 = TODO(),
            myChildUnderscore2 = TODO(),
            myChildsUnderscore2 = TODO()
        ))
    }

    override suspend fun getThirdSomeOthersData(call: ApplicationCall) {
        // Request
        // val id = call.request.queryParameters["id"]?.toString()
        // val path = call.request.queryParameters["path"].toString() ?: throw IllegalArgumentException("Invalid path")
        // Response
        /**
         * code = 200, message = "OK" response array: List<TestDto3>
         */
        val id = call.request.queryParameters["id"]
        val path = call.request.queryParameters["path"] ?: throw IllegalArgumentException("Invalid path")

        call.respond(listOf(
            TestDto3(
                id = TODO(),
                numberId = TODO(),
                anEnum = TODO(),
                mySubChild = TODO()
            )
        ))
    }

    override suspend fun getFourthResources(call: ApplicationCall) {
        TODO("Not yet implemented")
    }
}