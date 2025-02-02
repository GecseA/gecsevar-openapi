package hu.gecsevar.openapi

import com.samskivert.mustache.Mustache
import io.swagger.parser.OpenAPIParser
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.parser.core.models.ParseOptions
import io.swagger.v3.parser.util.InlineModelResolver
import org.openapitools.codegen.utils.ModelUtils
import java.io.File
import java.io.StringWriter
import kotlin.test.*


object TestUtils {

    fun parseFlattenSpec(specFilePath: String?): OpenAPI {
        val openAPI: OpenAPI = parseSpec(specFilePath)
        val inlineModelResolver: InlineModelResolver = InlineModelResolver()
        inlineModelResolver.flatten(openAPI)
        return openAPI
    }

    fun parseSpec(specFilePath: String?): OpenAPI {
        val openAPI = OpenAPIParser().readLocation(specFilePath, null, ParseOptions()).openAPI
        // Invoke helper function to get the original swagger version.
        // See https://github.com/swagger-api/swagger-parser/pull/1374
        // Also see https://github.com/swagger-api/swagger-parser/issues/1369.
        ModelUtils.getOpenApiVersion(openAPI, specFilePath, null)
        return openAPI
    }
}

// TODO https://github.com/OpenAPITools/openapi-generator/blob/master/modules/openapi-generator/src/test/java/org/openapitools/codegen/kotlin/AbstractKotlinCodegenTest.java
class SampleTest {

    private val codegen: KtorServerGenerator = KtorServerGenerator()

    @BeforeTest
    fun before() {

    }

    @Test
    fun testCamelCase() {

//        assertEquals("Dollar", codegen.toModelName("$"));
//        assertEquals("DollarDollar", codegen.toModelName("$$"));
//        assertEquals("PonyQuestionMark", codegen.toModelName("Pony?"));
//        assertEquals("DollarName", codegen.toModelName("\$name"));
//        assertEquals("NamHashE", codegen.toModelName("nam#e"));
//        assertEquals("DollarAnotherFakeQuestionMark", codegen.toModelName("\$another-fake?"));
//        assertEquals("PonyGreaterThanEqualGreaterThanEqual", codegen.toModelName("Pony>=>="));
        val path = System.getProperty("user.dir")
        println("Working Directory = $path")
        codegen.inputSpec = "F:\\developments\\domains\\openapi.gecsevar.hu\\openapi-mygenerator\\gecsevar-openapi\\src\\test\\resources\\simple.yml"     // "./src/test/resources/simple.yml"
        codegen.outputDir = "src/test/resources/generated/"
        codegen.setApiPackage("hu.gecsevar.plugins.api")
        codegen.setModelPackage("hu.gecsevar.database.view")
        codegen.processOpts()

//        val faszom = OpenAPIParser().readLocation("src\\test\\resources\\simple.yml", null, null)

        val openAPI = TestUtils.parseFlattenSpec("src\\test\\resources\\simple.yml")
        val test1 = openAPI.components.schemas["ArticleModel"]

        val text = "One, two, {{three}}. Three sir!"
        val tmpl = Mustache.compiler().compile(text)
        val data: MutableMap<String, String> = HashMap()
        data["three"] = "five"
        System.out.println(tmpl.execute(data))


        val source = "{{#lambda.convert_data_type_to_camel_case}}_camel_case_name{{/lambda.convert_data_type_to_camel_case}}"
        val expected = "CamelCaseName"
        val mustache = Mustache.compiler().compile(File("src/main/resources/gv-ktor-server/api.mustache").readText(Charsets.UTF_8))
        val out = StringWriter()

        val myContext: MutableMap<String, String> =
            mutableMapOf(
                "apiPackage" to "hu.gecsevar.plugins.api"
            )

        mustache.execute(myContext, out)

        val res = codegen.toVarName("_camel_case_name")
        val res1 = codegen.toApiName("_camel_case_name")
        val res2 = codegen.toModelName("_camel_case_name")

        assertEquals(expected, res2)
    }
}