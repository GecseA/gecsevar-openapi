package hu.gecsevar.openapi.app

import hu.gecsevar.openapi.app.database.view.*
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import java.math.BigDecimal
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class, ExperimentalSerializationApi::class)
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
        ),
        otherEnum = MyOtherEnum.not_unique,
        eastSideEnum = MyEastSideEnum.AKK,
    )

    val dto4 = TestDto4(
        id = 54321,
        price = "1234.56",
        amount = BigDecimal.valueOf(123456.31),
    )

    val dynamicData = Json.decodeFromString<TestDtoDynamicContent>("" +
            "{\"id\":123456}")
    // FIXME OpenAPI 3.1 feature works but, on GitHub the build action fails. Uncomment when org.openapitools:openapi-generator fully supports OpenAPI 3.1
    //        "{\"id\":123456,\"myJson\":{\"a\":55, \"b\": \"Hello World!\", \"c\": {\"aa\": 123456, \"bb\": 123456.31}}}")

    println(Json.encodeToString(dto1))
    println(Json.encodeToString(dto2))
    println(Json.encodeToString(dto22))
    println(Json.encodeToString(dto3))
    println(Json.encodeToString(dynamicData))

    // BigDecimal custom serializer
    val module = SerializersModule {
        contextual(BigDecimal::class, BigDecimalSerializer)
    }
    val json = Json { serializersModule = module }
    println(json.encodeToString(TestDto4.serializer(), dto4))
}

// BigDecimal custom serializer
object BigDecimalSerializer : KSerializer<BigDecimal> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("BigDecimal", PrimitiveKind.STRING)

    override fun serialize(encoder: Encoder, value: BigDecimal) {
        encoder.encodeString(value.toString())
    }

    override fun deserialize(decoder: Decoder): BigDecimal {
        return BigDecimal(decoder.decodeString())
    }
}