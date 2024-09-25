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
        System.out.println("################################################################################");
        System.out.println("# Thanks for using OpenAPI Generator.                                          #");
        System.out.println("# Please consider donation to help us maintain this project \uD83D\uDE4F       #");
        System.out.println("# https://opencollective.com/openapi_generator/donate                          #");
        System.out.println("#                                                                              #");
        System.out.println("# This generator's contributed by  #");
        System.out.println("# Please support his work directly  #");
        System.out.println("################################################################################");
    }
}
