package hu.gecsevar.openapi.app

import hu.gecsevar.openapi.app.database.view.MyEnum
import hu.gecsevar.openapi.app.database.view.TestDto1
import hu.gecsevar.openapi.app.database.view.TestDto2
import hu.gecsevar.openapi.app.database.view.TestDto3
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.json.Json
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun main(args: Array<String>) {

    println("Hello World!")

    val dto1 = TestDto1(
        id = 111,
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
    )

    val dto2 = TestDto2(
        id = 324243,
        myChildUnderscore1 = dto1,
        myChildsUnderscore1 = listOf(dto1),
        myChildUnderscore2 = null,
        myChildsUnderscore2 = null
    )

    val dto22 = TestDto2(
        id = 324243,
        myChildUnderscore1 = dto1,
        myChildsUnderscore1 = listOf(dto1),
        myChildUnderscore2 = dto1,
        myChildsUnderscore2 = listOf(dto1),
    )

    val dto3 = TestDto3(
        id = 3423523,
        numberId = -1,
        anEnum = MyEnum.TWO,
        mySubChild = listOf(
            dto2,
            dto22,
        )
    )

    println(Json.encodeToString(dto1))
    println(Json.encodeToString(dto2))
    println(Json.encodeToString(dto22))
    println(Json.encodeToString(dto3))
}