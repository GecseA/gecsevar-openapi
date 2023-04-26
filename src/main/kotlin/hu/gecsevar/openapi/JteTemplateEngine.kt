package hu.gecsevar.openapi

import gg.jte.ContentType
import gg.jte.TemplateEngine
import gg.jte.TemplateOutput
import gg.jte.output.StringOutput
import gg.jte.resolve.DirectoryCodeResolver
import org.openapitools.codegen.api.TemplatingEngineAdapter
import org.openapitools.codegen.api.TemplatingExecutor
import org.slf4j.LoggerFactory
import java.io.Reader
import java.io.StringReader
import java.nio.file.Path


class JteTemplateEngine : TemplatingEngineAdapter {

    private var logger = LoggerFactory.getLogger(TemplatingEngineAdapter::class.java)
    private val codeResolver = DirectoryCodeResolver(Path.of("."))
    val compiler = TemplateEngine.create(codeResolver, ContentType.Plain)
    init {
//        val codeResolver: CodeResolver = DirectoryCodeResolver(Path.of("templateDir")) // This is the directory where your .jte files are located.
//        TemplateEngine.create(codeResolver, CodeType.Kotlin)
    }
    override fun getIdentifier(): String {
        return "jte"
    }
    override fun getFileExtensions(): Array<String> {
        return arrayOf("jte")
    }
    /**
     * Compiles a template into a string
     * @param executor    From where we can fetch the templates content (e.g. an instance of DefaultGenerator)
     * @param bundle       The map of values to pass to the template
     * @param templateFile The name of the template (e.g. model.mustache )
     * @return the processed template result
     * @throws IOException an error occurred in the template processing
     */
    override fun compileTemplate(
        executor: TemplatingExecutor?,
        bundle: MutableMap<String, Any>?,
        templateFile: String?
    ): String {
        TODO("Not yet implemented")
        val tmpl = compiler
                .withLoader(name -> findTemplate(executor, name))
                .defaultValue("")
                .compile(executor.getFullTemplateContents(templateFile));
        return tmpl.execute(bundle);

        val output: TemplateOutput = StringOutput()
        templateEngine.render("example.jte", model, output)

        return output.toString()
    }
    @SuppressWarnings("java:S108") // catch-all is expected, and is later thrown
    fun findTemplate(generator: TemplatingExecutor, name: String): Reader {
        getFileExtensions().forEach {extension->
            val templateName = name + "." + extension
            try {
                return StringReader(generator.getFullTemplateContents(templateName));
            } catch (exception: Exception) {
                logger.error("Failed to read full template {}, {}", templateName, exception.toString());
            }
        }

        throw new TemplateNotFoundException(name);
    }
}
