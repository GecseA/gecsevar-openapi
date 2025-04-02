package hu.gecsevar.openapi

import org.openapitools.codegen.CodegenType
import org.openapitools.codegen.SupportingFile


class KtorServerGenerator : AbstractGenerator() {

    val PROJECT_NAME = "KtorServerGenerator"
    val sourceFolder = "src"
    val apiVersion = "1.0.5"

    init {
        outputFolder = "generated/"
        modelTemplateFiles["model.mustache"] = ".kt"
        apiTemplateFiles["api.mustache"] = ".kt"
        supportingFiles.add(SupportingFile("README.mustache", "", "README.md"))
        templateDir = "gv-ktor-server"
        embeddedTemplateDir = templateDir
        apiPackage = "Apis"
        modelPackage = "Models"

        // TODO
        // time type parameter
        //
    }

    // source folder where to write the files
    override fun getTag(): CodegenType {
        return CodegenType.SERVER
    }

    override fun getName(): String {
        return "gv-ktor-server"
    }

    override fun getHelp(): String {
        return "Generates a gecsevar's kotlin server"
    }

    override fun postProcess() {
        println("################################################################################");
        println("# Thanks for using OpenAPI Generator.                                          #");
        println("#                                                                              #");
        println("# This generator's contributed by AstrA - https://gecsevar.hu                  #");
        println("# Please support his work directly by on his GitHub at                         #");
        println("# Give & Send: stars, feature suggestions, fixes, etc.                         #");
        println("#                           https://github.com/GecseA/gecsevar-openapi         #");
        println("################################################################################");
    }
}
