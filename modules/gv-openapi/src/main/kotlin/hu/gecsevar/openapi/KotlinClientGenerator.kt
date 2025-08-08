package hu.gecsevar.openapi

import com.google.common.collect.ImmutableMap
import com.samskivert.mustache.Mustache
import com.samskivert.mustache.Template
import org.openapitools.codegen.CodegenType
import org.openapitools.codegen.SupportingFile
import java.io.Writer

class KotlinClientGenerator : AbstractGenerator() {
    init {
        outputFolder = "generated/"
        modelTemplateFiles["model.mustache"] = ".kt"
        apiTemplateFiles["api.mustache"] = ".kt"
        supportingFiles.add(SupportingFile("README.mustache", "", "README.md"))
        //supportingFiles.add(SupportingFile("http_service.mustache", "", "HttpService.kt"))
        templateDir = "gv-kotlin-client"
        embeddedTemplateDir = templateDir
        apiPackage = "Apis"
        modelPackage = "Models"
    }

    override fun getTag(): CodegenType {
        return CodegenType.CLIENT
    }

    override fun getName(): String {
        return "gv-kotlin-client"
    }

    override fun getHelp(): String {
        return "Generates a kotlin data classes and HTTP calls client"
    }

//    override fun setTemplatingEngine(templatingEngine: TemplatingEngineAdapter?) {
//        super.setTemplatingEngine(JteTemplateEngine(templateDir))
//    }
// TODO replace this dummy engine
//    override fun setTemplatingEngine(templatingEngine: TemplatingEngineAdapter?) {
//        super.setTemplatingEngine(HandlebarsEngineAdapter())
//    }
    override fun defaultTemplatingEngine(): String {
        return "mustache"
    }

    override fun postProcess() {
        println("################################################################################")
        println("# Thanks for using my OpenAPI Generator.                                       #")
        println("#                                                                              #")
        println("# This generator's contributed by AstrA - https://gecsevar.hu                  #")
        println("# Please support his work directly by on his GitHub at                         #")
        println("# Give & Send: stars, feature suggestions, fixes, etc.                         #")
        println("#                           https://github.com/GecseA/gecsevar-openapi         #")
        println("################################################################################")
    }

    override fun addMustacheLambdas(): ImmutableMap.Builder<String, Mustache.Lambda> {
        return super.addMustacheLambdas()
            .put("", CreateClientCode)
    }

    object CreateClientCode : Mustache.Lambda {
        override fun execute(frag: Template.Fragment?, out: Writer?) {

        }
    }
}
