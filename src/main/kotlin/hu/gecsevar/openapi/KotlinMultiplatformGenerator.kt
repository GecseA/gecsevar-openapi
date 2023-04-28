package hu.gecsevar.openapi

import gg.jte.CodeResolver
import gg.jte.TemplateEngine
import gg.jte.compiler.CodeType
import gg.jte.resolve.DirectoryCodeResolver
import org.openapitools.codegen.CodegenType
import org.openapitools.codegen.SupportingFile
import org.openapitools.codegen.api.TemplatingEngineAdapter
import org.openapitools.codegen.api.TemplatingExecutor
import org.openapitools.codegen.templating.HandlebarsEngineAdapter
import java.nio.file.Path

class KotlinMultiplatformGenerator : AbstractGenerator() {
    init {
        outputFolder = "generated/"
        modelTemplateFiles["model.handlebars"] = ".kt"
        apiTemplateFiles["api.handlebars"] = ".kt"
        supportingFiles.add(SupportingFile("README.handlebars", "", "README.md"))
        //supportingFiles.add(SupportingFile("http_service.mustache", "", "HttpService.kt"))
        templateDir = "gv-kotlin-multiplatform"
        embeddedTemplateDir = templateDir
        apiPackage = "Apis"
        modelPackage = "Models"
    }

    // source folder where to write the files
    protected val sourceFolder = "src"
    val apiVersion = "1.0.5"

    override fun getTag(): CodegenType {
        return CodegenType.CLIENT
    }

    override fun getName(): String {
        return "gv-kotlin-multiplatform"
    }

    override fun getHelp(): String {
        return "Generates a gecsevar's kotlin multiplatform (KMM) client"
    }

//    override fun setTemplatingEngine(templatingEngine: TemplatingEngineAdapter?) {
//        super.setTemplatingEngine(JteTemplateEngine(templateDir))
//    }

    override fun setTemplatingEngine(templatingEngine: TemplatingEngineAdapter?) {
        super.setTemplatingEngine(HandlebarsEngineAdapter())
    }
    override fun defaultTemplatingEngine(): String {
        return "handlebars"
    }

    override fun postProcess() {
        System.out.println("################################################################################");
        System.out.println("################################################################################");
        System.out.println("################################################################################");
        System.out.println("# Thanks for using OpenAPI Generator.                                          #");
    }
}
