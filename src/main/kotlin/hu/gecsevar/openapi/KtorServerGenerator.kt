package hu.gecsevar.openapi

import com.github.jknack.handlebars.internal.lang3.StringUtils
import com.google.common.collect.ImmutableMap
import com.samskivert.mustache.Mustache
import com.samskivert.mustache.Template
import org.apache.commons.io.IOUtils.writer
import org.mozilla.javascript.ScriptRuntime.isPrimitive
import org.openapitools.codegen.*
import org.openapitools.codegen.CodegenConstants.ENUM_PROPERTY_NAMING_TYPE
import org.openapitools.codegen.config.CodegenConfigurator.LOGGER
import org.openapitools.codegen.model.ModelsMap
import java.awt.SystemColor.text
import java.io.Writer
import java.util.*
import java.util.regex.Pattern


class KtorServerGenerator : DefaultCodegen(), CodegenConfig {

    val PROJECT_NAME = "KtorServerGenerator"
    val sourceFolder = "src"
    val apiVersion = "1.0.5"

    init {

/*
        reservedWords = HashSet<String>(
            Arrays.asList(
                "sample1",  // replace with static values
                "sample2"
            )
        )
*/
        //additionalProperties.put("apiVersion", apiVersion);

        //supportingFiles.add(SupportingFile("myFile.mustache","", "myFile.sample"))
/*
        languageSpecificPrimitives = HashSet<String>(
            Arrays.asList(
                "Type1",      // replace these with your types
                "Type2"
            )
        )

 */

        languageSpecificPrimitives = HashSet(
            mutableListOf(
                "kotlin.Byte",
                "kotlin.Short",
                "kotlin.Int",
                "kotlin.Long",
                "kotlin.Float",
                "kotlin.Double",
                "kotlin.Boolean",
                "kotlin.Char",
                "kotlin.String",
                "kotlin.Array",
                "kotlin.collections.List",
                "kotlin.collections.Map",
                "kotlin.collections.Set"
            )
        )
        defaultIncludes = HashSet(
            mutableListOf(
                "kotlin.Byte",
                "kotlin.Short",
                "kotlin.Int",
                "kotlin.Long",
                "kotlin.Float",
                "kotlin.Double",
                "kotlin.Boolean",
                "kotlin.Char",
                "kotlin.Array",
                "kotlin.collections.List",
                "kotlin.collections.Set",
                "kotlin.collections.Map"
            )
        )
        typeMapping = HashMap()
        typeMapping["string"] = "kotlin.String"
        typeMapping["boolean"] = "kotlin.Boolean"
        typeMapping["integer"] = "kotlin.Int"
        typeMapping["float"] = "kotlin.Float"
        typeMapping["long"] = "kotlin.Long"
        typeMapping["double"] = "kotlin.Double"
        typeMapping["number"] = "java.math.BigDecimal"
        typeMapping["date-time"] = "Instant" // "java.time.LocalDateTime"
        typeMapping["date"] = "java.time.LocalDateTime"
        typeMapping["file"] = "java.io.File"
        typeMapping["array"] = "kotlin.collections.List" //"kotlin.Array"
        typeMapping["list"] = "kotlin.collections.List" // "kotlin.Array"
        typeMapping["map"] = "kotlin.collections.Map"
        typeMapping["object"] = "kotlin.Any"
        typeMapping["binary"] = "kotlin.Array<kotlin.Byte>"
        typeMapping["Date"] = "java.time.LocalDateTime"
        typeMapping["DateTime"] = "Instant" // "java.time.LocalDateTime"
        typeMapping["ByteArray"] = "kotlin.Array<Byte>"

//    instantiationTypes["array"] = "arrayOf"
        instantiationTypes["list"] = "listOf"
        instantiationTypes["map"] = "mapOf"

        importMapping = HashMap()
        importMapping["BigDecimal"] = "java.math.BigDecimal"
        importMapping["UUID"] = "java.util.UUID"
        importMapping["File"] = "java.io.File"
        importMapping["Date"] = "java.util.Date"
        importMapping["Timestamp"] = "kotlinx.datetime.Instant"
        importMapping["DateTime"] = "kotlinx.datetime.Instant" // "java.time.LocalDateTime2"
        importMapping["LocalDateTime"] = "kotlinx.datetime.Instant"
        importMapping["LocalDate"] = "kotlinx.datetime.Instant" // "java.time.LocalDate2"
        importMapping["LocalTime"] = "java.time.LocalTime2"
        importMapping["string"] = "kotlin.String"
        importMapping["long"] = "kotlin.Long"
        importMapping["integer"] = "kotlin.Int"
        importMapping["array"] = "kotlin.collections.List"
        importMapping["ByteArray"] = "kotlin.ByteArray"

        outputFolder = "generated/"
        modelTemplateFiles["model.mustache"] = ".kt"
        apiTemplateFiles["api.mustache"] = ".kt"
        supportingFiles.add(SupportingFile("README.mustache", "", "README.md"))
        templateDir = "gv-ktor-server"
        embeddedTemplateDir = templateDir
        apiPackage = "Apis"
        modelPackage = "Models"

        val artifactId = "";
        val artifactVersion = "1.0.0";
        val groupId = "io.swagger";
        val packageName = "";
        val enumPropertyNaming = ENUM_PROPERTY_NAMING_TYPE.camelCase

        /*
        // TODO: Configurable server engine. Defaults to netty in build.gradle.
        val library = CliOption(CodegenConstants.LIBRARY, "library template (sub-template) to use")
        library.default = DEFAULT_LIBRARY
        library.enum = supportedLibraries

        cliOptions.add(library)


        // no good on enum
        cliOptions.clear()
        addOption(CodegenConstants.SOURCE_FOLDER, CodegenConstants.SOURCE_FOLDER_DESC, sourceFolder)
        addOption(CodegenConstants.PACKAGE_NAME, "Generated artifact package name (e.g. io.swagger).", packageName)
        addOption(CodegenConstants.GROUP_ID, "Generated artifact package's organization (i.e. maven groupId).", groupId)
        addOption(CodegenConstants.ARTIFACT_ID, "Generated artifact id (name of jar).", artifactId)
        addOption(CodegenConstants.ARTIFACT_VERSION, "Generated artifact's package version.", artifactVersion)



        // no good on enum
        val enumPropertyNamingOpt =
            CliOption(CodegenConstants.ENUM_PROPERTY_NAMING, CodegenConstants.ENUM_PROPERTY_NAMING_DESC)
        cliOptions.add(enumPropertyNamingOpt.defaultValue(enumPropertyNaming.name))

         */

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

    override fun addMustacheLambdas(): ImmutableMap.Builder<String, Mustache.Lambda> {
        return super.addMustacheLambdas().put("convert_path_to_fun", ConvertPathToFunction)
    }

    object ConvertPathToFunction : Mustache.Lambda {
        override fun execute(frag: Template.Fragment?, out: Writer?) {
            var text = frag?.execute()
            text = text?.replace("/\\{(.*?)\\}".toRegex()) {
                "_with" + it.value.replace("{", "").replace("}", "").uppercase()
            }?.replaceFirst("/", "")
            var nextUpper = true
            var myText = ""
            text?.forEach {
                if (nextUpper) {
                    myText += it.uppercase().first()
                    nextUpper = false
                } else if (it == '/') {
                    nextUpper = true
                } else {
                    myText += it
                }
            }
            out?.write(myText)
            LOGGER.warn("Mustache.Lambda: $myText")
        }
    }

    override fun postProcessSupportingFileData(objs: MutableMap<String, Any>?): MutableMap<String, Any> {
        return super.postProcessSupportingFileData(objs)
    }

    override fun escapeUnsafeCharacters(input: String?): String {
        if (input != null) {
            return input.replace("*/", "*_/").replace("/*", "/_*")
        }
        return ""
    }

   override fun escapeQuotationMark(input: String?): String {
       if (input != null) {
           return input.replace("\"", "\\\"")
       }
       return ""
   }

    override fun escapeReservedWord(name: String?): String {
        // TODO: Allow enum escaping as an option (e.g. backticks vs append/prepend underscore vs match model property escaping).
        return String.format("`%s`", name)
    }

    override fun toEnumName(property: CodegenProperty?): String {
        LOGGER.warn("toEnumName: ${property?.name}")
        return StringUtils.capitalize(property?.name)
    }

    override fun toEnumValue(value: String, datatype: String?): String? {
        LOGGER.warn("toEnumValue: ${value} | $datatype")
        if (isPrimitive(datatype)) {
            return value
        }
        return if ("java.math.BigDecimal".equals(datatype, ignoreCase = true)) {
            "java.math.BigDecimal(\"$value\")"
        } else super.toEnumValue(value, datatype)
    }

   override fun toEnumVarName(value: String, datatype: String?): String {
       LOGGER.warn("toEnumVarName: ${value} | $datatype")
       var modified: String
       if (value.isEmpty()) {
           modified = "EMPTY"
       } else {
           modified = value
           //modified = "sanitizeKotlinSpecificNames(modified)"
       }
       modified = modified.uppercase(Locale.getDefault())
       return if (isReservedWord(modified)) {
           escapeReservedWord(modified)
       } else modified
   }

    override fun postProcessModels(objs: ModelsMap?): ModelsMap {
        return postProcessModelsEnum(objs)
        //return super.postProcessModels(objs)
    }

    override fun lowerCamelCase(name: String?): String {
        return super.lowerCamelCase("csirke $name")
    }

    override fun postProcess() {
        System.out.println("################################################################################");
        System.out.println("# Thanks for using OpenAPI Generator.                                          #");
        System.out.println("# Please consider donation to help us maintain this project \uD83D\uDE4F                #");
        System.out.println("# https://opencollective.com/openapi_generator/donate                          #");
        System.out.println("#                                                                              #");
        System.out.println("# This generator's contributed by  #");
        System.out.println("# Please support his work directly via https://patreon.com/jimschubert \uD83D\uDE4F     #");
        System.out.println("################################################################################");

    }
 //   private fun isPrimitive(datatype: String?): Boolean {
 //       return ("kotlin.Byte".equals(datatype, ignoreCase = true)
 //               || "kotlin.Short".equals(datatype, ignoreCase = true)
 //               || "kotlin.Int".equals(datatype, ignoreCase = true)
 //               || "kotlin.Long".equals(datatype, ignoreCase = true)
 //               || "kotlin.Float".equals(datatype, ignoreCase = true)
 //               || "kotlin.Double".equals(datatype, ignoreCase = true)
 //               || "kotlin.Boolean".equals(datatype, ignoreCase = true))
 //   }
//

//
 //   private fun sanitizeKotlinSpecificNames(name: String): String {
 //       LOGGER.warn("ASTRA: sanitizeKotlinSpecificNames ${name}")
 //       var word = removeNonNameElementToCamelCase(name)
 //       for ((key, value) in specialCharReplacements) {
 //           // Underscore is the only special character we'll allow
 //           if (key != "_") {
 //               word = word.replace("\\Q$key\\E".toRegex(), value)
 //           }
 //       }
//
 //       // Fallback, replace unknowns with underscore.
 //       word = word.replace("\\W+".toRegex(), "_")
 //       if (word.matches("\\d.*".toRegex())) {
 //           word = "_$word"
 //       }
//
 //       // _, __, and ___ are reserved in Kotlin. Treat all names with only underscores consistently, regardless of count.
 //       if (word.matches("^_*$".toRegex())) {
 //           word = word.replace("\\Q_\\E".toRegex(), "Underscore")
 //       }
 //       return word
 //   }
}
