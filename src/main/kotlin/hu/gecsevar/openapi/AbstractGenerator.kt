package hu.gecsevar.openapi

import com.github.jknack.handlebars.internal.lang3.StringUtils
import com.google.common.collect.ImmutableMap
import com.samskivert.mustache.Mustache
import com.samskivert.mustache.Template
import org.mozilla.javascript.ScriptRuntime
import org.openapitools.codegen.CodegenConfig
import org.openapitools.codegen.CodegenConstants
import org.openapitools.codegen.CodegenProperty
import org.openapitools.codegen.DefaultCodegen
import org.openapitools.codegen.model.ModelsMap
import java.io.Writer
import java.util.*


abstract class AbstractGenerator : DefaultCodegen(), CodegenConfig {

    init {
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
        typeMapping["string"] = "String"
        typeMapping["boolean"] = "Boolean"
        typeMapping["integer"] = "Int"
        typeMapping["integer+int32"] = "Int"
        typeMapping["integer+int64"] = "Long"
        typeMapping["float"] = "Float"
        typeMapping["long"] = "Long"
        typeMapping["double"] = "Double"
        typeMapping["number"] = "Int"
        typeMapping["date-time"] = "Instant"
        typeMapping["date"] = "LocalDate"
        typeMapping["file"] = "java.io.File"
        typeMapping["array"] = "List"
        typeMapping["list"] = "List"
        typeMapping["map"] = "Map"
        typeMapping["object"] = "Any"
        typeMapping["binary"] = "Array<Byte>"
        typeMapping["Date"] = "LocalDate"
        typeMapping["DateTime"] = "Instant"
        typeMapping["ByteArray"] = "Array<Byte>"

        instantiationTypes["list"] = "listOf"
        instantiationTypes["map"] = "mapOf"

        languageSpecificPrimitives = HashSet(
            mutableListOf(
                "Byte",
                "Short",
                "Int",
                "Long",
                "Float",
                "Double",
                "Boolean",
                "Char",
                "String",
                "Array",
                "List",
                "Map",
                "Set",

                "number",
                "float",
            )
        )

        importMapping = HashMap()
        importMapping["BigDecimal"] = "java.math.BigDecimal"
        importMapping["UUID"] = "java.util.UUID"
        importMapping["File"] = "java.io.File"
        importMapping["Date"] = "java.util.Date"
        importMapping["Timestamp"] = "kotlinx.datetime.Instant"
        importMapping["DateTime"] = "kotlinx.datetime.Instant"
        importMapping["date-time"] = "kotlinx.datetime.LocalDateTime"
        importMapping["date"] = "kotlinx.datetime.LocalDate"
        importMapping["time"] = "kotlinx.datetime.LocalTime"
        importMapping["string"] = "kotlin.String"
        importMapping["long"] = "kotlin.Long"
        importMapping["boolean"] = "kotlin.Boolean"
        importMapping["integer"] = "kotlin.Int"
        importMapping["array"] = "kotlin.collections.List"
        importMapping["ByteArray"] = "kotlin.ByteArray"

        val artifactId = "";
        val artifactVersion = "1.0.0";
        val groupId = "io.swagger";
        val packageName = "";
        val enumPropertyNaming = CodegenConstants.ENUM_PROPERTY_NAMING_TYPE.camelCase

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

    override fun addMustacheLambdas(): ImmutableMap.Builder<String, Mustache.Lambda> {
        return super.addMustacheLambdas()
            .put("convert_path_to_fun", ConvertPathToFunction)
            .put("convert_to_pascal_case", ConvertPascalCase)
            .put("convert_data_type_to_snake_case", ConvertDataTypeToSnakeCase)
    }

    object ConvertPascalCase: Mustache.Lambda {
        override fun execute(frag: Template.Fragment?, out: Writer?) {
            val text = frag?.execute()

            //CodegenConfigurator.LOGGER.warn("ConvertDataClassToCamelCase (BEFORE): $text")
            val snakeToCamel = Regex("_([a-zA-Z0-9])")
            val result = text?.replace(snakeToCamel) {
                it.value.removePrefix("_").uppercase()
            }?.replaceFirstChar { it.uppercase() }

            out?.write(result.toString())
            //CodegenConfigurator.LOGGER.warn("ConvertDataClassToCamelCase (AFTER): $result")
        }
    }
    /**
     * Mustache extension function
     *
     * snake_case_text -> SnakeCaseText
     */
    object ConvertDataTypeToCamelCase: Mustache.Lambda {
        override fun execute(frag: Template.Fragment?, out: Writer?) {
            val text = frag?.execute()

            val snakeToCamel = Regex("_([a-zA-Z0-9])")
            val result = text?.replace(snakeToCamel) {
                it.value.removePrefix("_").uppercase()
            }?.replaceFirstChar { it.uppercase() }
            out?.write(result.toString())
        }
    }

    /**
     * Mustache extension function
     *
     * camelCaseText -> camel_case_text
     */
    object ConvertDataTypeToSnakeCase: Mustache.Lambda {
        override fun execute(frag: Template.Fragment?, out: Writer?) {
            val text = frag?.execute()

            val camelToSnake = Regex("([A-Z])")
            val result = text?.replace(camelToSnake) { it ->
                it.value.let { part ->
                    "_".plus(part.lowercase())
                }
            }
            out?.write(result.toString())
        }
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
                } else if (it == '-') {
                    nextUpper = true
                } else {
                    myText += it
                }
            }
            out?.write(myText)
            //CodegenConfigurator.LOGGER.warn("Mustache.Lambda: $myText")
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
        //CodegenConfigurator.LOGGER.warn("toEnumName: ${property?.name}")
        return StringUtils.capitalize(property?.name)
    }

    override fun toEnumValue(value: String, datatype: String?): String? {
        //CodegenConfigurator.LOGGER.warn("toEnumValue: ${value} | $datatype")
        if (ScriptRuntime.isPrimitive(datatype)) {
            return value
        }
        return if ("java.math.BigDecimal".equals(datatype, ignoreCase = true)) {
            "java.math.BigDecimal(\"$value\")"
        } else super.toEnumValue(value, datatype)
    }

    override fun toEnumVarName(value: String, datatype: String?): String {
        //CodegenConfigurator.LOGGER.warn("toEnumVarName: ${value} | $datatype")
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

    override fun toModelImport(name: String?): String {
        if (name?.contains("_") == true) {
            // CodegenConfigurator.LOGGER.warn("toModelImport (EXCLUDE): $name")
            val text = name
            var nextUpper = true
            var myText = ""
            text.forEach {
                if (nextUpper) {
                    myText += it.uppercase()
                    nextUpper = false
                } else if (it == '_') {
                    nextUpper = true
                } else {
                    myText += it
                }
            }
            return super.toModelImport(myText)
        }
        //CodegenConfigurator.LOGGER.warn("toModelImport: $name")
        return super.toModelImport(name)
    }

    override fun postProcessModels(objs: ModelsMap?): ModelsMap {
        return postProcessModelsEnum(objs)
        //return super.postProcessModels(objs)
    }

    override fun lowerCamelCase(name: String?): String {

        return super.lowerCamelCase(name)
    }

    override fun defaultIncludes(): MutableSet<String> {
        return defaultIncludes
    }

    override fun typeMapping(): MutableMap<String, String> {
        return typeMapping
    }

    override fun languageSpecificPrimitives(): MutableSet<String> {
        return languageSpecificPrimitives
    }

    override fun updatePropertyForArray(property: CodegenProperty?, innerProperty: CodegenProperty?) {
        //CodegenCologgernfigurator.LOGGER.warn("updatePropertyForArray: ${property} | $innerProperty")
        super.updatePropertyForArray(property, innerProperty)
    }
}