package hu.gecsevar.openapi.app

import hu.gecsevar.database.view.TestDto1
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
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
        myStringId = Uuid.random().toString(),
        myNumber = 234234,
        myNumber2 = 234235544,
        aDateTime = Clock.System.now(),
        aDate = Clock.System.now().toLocalDateTime(TimeZone.UTC).date,
        aTime = "wrong",
        myEnum = TestDto1.MyEnum.MY_ENUM_1,
        myArray = listOf(
            "string 1",
            "string 2",
            "string 3",
        )
    )

    println(Json.encodeToString(dto1))
//    val article = ArticleModel(
//        id = 0,
//        published = false,
//        slug = "my-slug",
//        categories = listOf("article"),
//        creator = "AstrA",
//        createTime = Clock.System.now(),
//        title = "My Title",
//        cardImageUrl = "",
//        bannerImageUrl = "",
//        description = "Good description",
//        content = "Fun content"
//    )

//    println(Json.encodeToString(article))
}