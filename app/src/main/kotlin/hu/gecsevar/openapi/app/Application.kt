package hu.gecsevar.openapi.app

import hu.gecsevar.database.view.ArticleModel
import kotlinx.datetime.Clock
import kotlinx.serialization.json.Json
import org.sample.stringutils.Strings
import org.sample.plumberutils.Numbers2

fun main(args: Array<String>) {

    println("Hello World!")
    val output: String = Strings.concat(" The answer is ", Numbers2.add(19, 23))
    println(output)

    val article = ArticleModel(
        id = 0,
        published = false,
        slug = "my-slug",
        categories = listOf("article"),
        creator = "AstrA",
        createTime = Clock.System.now(),
        title = "My Title",
        cardImageUrl = "",
        bannerImageUrl = "",
        description = "Good description",
        content = "Fun content"
    )

    println(Json.encodeToString(article))
}