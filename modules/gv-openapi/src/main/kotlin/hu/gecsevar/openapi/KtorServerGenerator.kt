package hu.gecsevar.openapi

import org.openapitools.codegen.CliOption
import org.openapitools.codegen.CodegenConstants
import org.openapitools.codegen.CodegenType
import org.openapitools.codegen.SupportingFile


class KtorServerGenerator : AbstractGenerator() {

    val PROJECT_NAME = "KtorServerGenerator"
    var sourceFolder = "src"
    val apiVersion = "1.0.5"
    var dddFormatting = false

    init {
        outputFolder = "generated/"
        modelTemplateFiles["model.mustache"] = ".kt"
        apiTemplateFiles["api.mustache"] = ".kt"
        supportingFiles.add(SupportingFile("README.mustache", "", "README.md"))
        templateDir = "gv-ktor-server"
        embeddedTemplateDir = templateDir
        modelPackage = "Models"

        // TODO
        // time type parameter
        //
        cliOptions.add(CliOption(CodegenConstants.API_PACKAGE, CodegenConstants.API_PACKAGE_DESC, apiPackage))
        cliOptions.add(CliOption(CodegenConstants.SOURCE_FOLDER, CodegenConstants.SOURCE_FOLDER_DESC, sourceFolder))
        cliOptions.add(CliOption("dddFormatting", "Domain Driven Design styled model and api", "boolean"))

    }

    override fun processOpts() {
        super.processOpts()

        if (additionalProperties.containsKey(CodegenConstants.SOURCE_FOLDER)) {
            sourceFolder = additionalProperties[CodegenConstants.SOURCE_FOLDER] as String
        }
        if (additionalProperties.containsKey("dddFormatting")) {
            dddFormatting = additionalProperties["dddFormatting"] as Boolean
            additionalProperties["dddFormatting"] = dddFormatting
            println("dddFormatting: $dddFormatting")
        }
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
        println("# Thanks for using my OpenAPI Generator.                                       #");
        println("#                                                                              #");
        println("# This generator's contributed by AstrA - https://gecsevar.hu                  #");
        println("# Please support his work directly by on his GitHub at                         #");
        println("# Give & Send: stars, feature suggestions, fixes, etc.                         #");
        println("#                           https://github.com/GecseA/gecsevar-openapi         #");
        println("################################################################################");
    }
}
