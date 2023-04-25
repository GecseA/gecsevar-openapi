package hu.gecsevar.openapi

import org.openapitools.codegen.CodegenType
import org.openapitools.codegen.SupportingFile

class KotlinMultiplatformGenerator : AbstractGenerator() {

    init {
        outputFolder = "generated/"
        modelTemplateFiles["model.mustache"] = ".kt"
        apiTemplateFiles["api.mustache"] = ".kt"
        supportingFiles.add(SupportingFile("README.mustache", "", "README.md"))
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

    override fun postProcess() {
        System.out.println("################################################################################");
        System.out.println("################################################################################");
        System.out.println("################################################################################");
        System.out.println("# Thanks for using OpenAPI Generator.                                          #");
    }
}
