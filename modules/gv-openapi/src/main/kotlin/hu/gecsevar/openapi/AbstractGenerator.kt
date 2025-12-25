package hu.gecsevar.openapi

import com.google.common.collect.ImmutableMap
import com.samskivert.mustache.Mustache
import com.samskivert.mustache.Template
import org.openapitools.codegen.*
import org.openapitools.codegen.model.ModelsMap
import org.openapitools.codegen.utils.CamelizeOption
import org.openapitools.codegen.utils.StringUtils.camelize
import org.openapitools.codegen.utils.StringUtils.underscore
import java.io.Writer
import java.text.Normalizer
import java.util.*
import java.util.regex.Pattern
import kotlin.time.ExperimentalTime


abstract class AbstractGenerator : DefaultCodegen(), CodegenConfig {

    protected var enumPropertyNaming: CodegenConstants.ENUM_PROPERTY_NAMING_TYPE = CodegenConstants.ENUM_PROPERTY_NAMING_TYPE.original

    // https://github.com/OpenAPITools/openapi-generator/blob/master/modules/openapi-generator/src/main/java/org/openapitools/codegen/languages/AbstractKotlinCodegen.java
    init {
        // Skip "import ..." for these types
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
                "float",

                "file",
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
                "kotlin.collections.Map",
            )
        )

        // Map OpenAPI types to Kotlin types
        typeMapping = HashMap()
        typeMapping["string"] = "String"
        typeMapping["UUID"] = "Uuid"
        typeMapping["boolean"] = "Boolean"
        typeMapping["integer"] = "Int"
        typeMapping["integer+int32"] = "Int"
        typeMapping["integer+int64"] = "Long"
        typeMapping["float"] = "Float"
        typeMapping["long"] = "Long"
        typeMapping["double"] = "Double"
        typeMapping["number"] = "@Contextual BigDecimal"
        typeMapping["date-time"] = "Instant"
        typeMapping["date"] = "LocalDate"
        typeMapping["time"] = "LocalTime"
        typeMapping["file"] = "ByteArray"   // File as ByteArray
        typeMapping["array"] = "List"
        typeMapping["list"] = "List"
        typeMapping["map"] = "JsonObject"
        typeMapping["object"] = "Any"
        typeMapping["binary"] = "File"
        typeMapping["Date"] = "LocalDate"
        typeMapping["DateTime"] = "Instant"
        typeMapping["ByteArray"] = "Array<Byte>"

        instantiationTypes["list"] = "listOf"

        // Add import for specific types, keep in mind the languageSpecificPrimitives variable!
        importMapping = HashMap()
        importMapping["UUID"] = "kotlin.uuid.Uuid; import kotlin.uuid.ExperimentalUuidApi"
        importMapping["File"] = "java.io.File"
        importMapping["Date"] = "java.util.Date"
        importMapping["Timestamp"] = "kotlin.time.Instant; import kotlin.time.ExperimentalTime" // upgrade to Exposed 1.x
        importMapping["DateTime"] = "kotlin.time.Instant; import kotlin.time.ExperimentalTime"  // upgrade to Exposed 1.x
        importMapping["date-time"] = "kotlinx.datetime.LocalDateTime"
        importMapping["date"] = "kotlinx.datetime.LocalDate"
        importMapping["time"] = "kotlinx.datetime.LocalTime"
        importMapping["string"] = "kotlin.String"
        importMapping["long"] = "kotlin.Long"
        importMapping["double"] = "kotlin.Double"
        importMapping["boolean"] = "kotlin.Boolean"
        importMapping["integer"] = "kotlin.Int"
        importMapping["array"] = "kotlin.collections.List"
        importMapping["set"] = "kotlin.collections.Set"
        importMapping["map"] = "kotlin.collections.Map"
        importMapping["ByteArray"] = "kotlin.ByteArray"
        importMapping["AnyType"] = "kotlin.Any"
        importMapping["map"] = "kotlinx.serialization.json.JsonObject"
        importMapping["number"] = "java.math.BigDecimal; import kotlinx.serialization.Contextual"

        val artifactId = "";
        val artifactVersion = "1.0.0";
        val groupId = "io.swagger";
        val packageName = "";
        val enumPropertyNaming = CodegenConstants.ENUM_PROPERTY_NAMING_TYPE.original

        val enumPropertyNamingOpt = CliOption(CodegenConstants.ENUM_PROPERTY_NAMING, CodegenConstants.ENUM_PROPERTY_NAMING_DESC)
        cliOptions.add(enumPropertyNamingOpt.defaultValue(enumPropertyNaming.name))

//        cliOptions.add(CliOption(CodegenConstants.PARCELIZE_MODELS, CodegenConstants.PARCELIZE_MODELS_DESC))
//        cliOptions.add(CliOption(CodegenConstants.SERIALIZABLE_MODEL, CodegenConstants.SERIALIZABLE_MODEL_DESC))
//        cliOptions.add(CliOption(CodegenConstants.SORT_PARAMS_BY_REQUIRED_FLAG, CodegenConstants.SORT_PARAMS_BY_REQUIRED_FLAG_DESC))
//        cliOptions.add(CliOption(CodegenConstants.SORT_MODEL_PROPERTIES_BY_REQUIRED_FLAG, CodegenConstants.SORT_MODEL_PROPERTIES_BY_REQUIRED_FLAG_DESC))
//
//        cliOptions.add(CliOption.newBoolean(MODEL_MUTABLE, MODEL_MUTABLE_DESC, false))
//        cliOptions.add(
//            CliOption.newString(
//                ADDITIONAL_MODEL_TYPE_ANNOTATIONS,
//                "Additional annotations for model type(class level annotations). List separated by semicolon(;) or new line (Linux or Windows)"
//            )
//        )

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

    override fun modelFileFolder(): String? {
        return super.modelFileFolder()
    }

    override fun apiFileFolder(): String? {
        return super.apiFileFolder()
    }

    override fun addMustacheLambdas(): ImmutableMap.Builder<String, Mustache.Lambda> {
        return super.addMustacheLambdas()
            .put("convert_path_to_fun", ConvertPathToFunction)
            .put("convert_to_pascal_case", ConvertPascalCase)
            .put("convert_data_type_to_snake_case", ConvertDataTypeToSnakeCase)
            .put("opt_in_requirement", OptInRequirement)
            .put("convert_to_first_capital", ConvertFirstCapital)
    }

    object OptInRequirement: Mustache.Lambda {
        override fun execute(frag: Template.Fragment?, out: Writer?) {
            val text = frag?.execute()

            if (text == "Uuid") {
                out?.write("@OptIn(ExperimentalUuidApi::class) ")
            }
            if (text == "Instant") {
                out?.write("@OptIn(ExperimentalTime::class) ")
            }
        }
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
    // TODO
    //val visitor-email = call.request.queryParameters["visitor-email"]
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
                "By" + it.value.replace("{", "").replace("}", "").uppercase()
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
        }
    }

    object ConvertFirstCapital: Mustache.Lambda {
        override fun execute(frag: Template.Fragment?, out: Writer?) {
            val text = frag?.execute()

            val result = text?.lowercase()?.replaceFirstChar { it.uppercase() }

            out?.write(result.toString())
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

    override fun toEnumVarName(value: String, datatype: String?): String? {
        if (enumNameMapping.containsKey(value)) {
            return enumNameMapping[value]
        }

        var modified: String?
        if (value.isEmpty()) {
            modified = "EMPTY"
        } else {
            modified = value.replace("-".toRegex(), "_")
            modified = sanitizeKotlinSpecificNames(modified)
        }

        when (enumPropertyNaming) {
            CodegenConstants.ENUM_PROPERTY_NAMING_TYPE.original ->                 // NOTE: This is provided as a last-case allowance, but will still result in reserved words being escaped.
                if (value.isEmpty()) {
                    modified = "EMPTY"
                } else {
                    modified = Normalizer.normalize(value, Normalizer.Form.NFD).replace("\\p{M}".toRegex(), "")
                }

            CodegenConstants.ENUM_PROPERTY_NAMING_TYPE.camelCase ->                 // NOTE: Removes hyphens and underscores
                modified = camelize(modified, CamelizeOption.LOWERCASE_FIRST_LETTER)

            CodegenConstants.ENUM_PROPERTY_NAMING_TYPE.PascalCase -> {
                // NOTE: Removes hyphens and underscores
                val result: String? = camelize(modified)
                modified = titleCase(result.toString())
            }

            CodegenConstants.ENUM_PROPERTY_NAMING_TYPE.snake_case ->                 // NOTE: Removes hyphens
                modified = underscore(modified)

            CodegenConstants.ENUM_PROPERTY_NAMING_TYPE.UPPERCASE -> modified = underscore(modified).uppercase(Locale.ROOT)
        }

        if (reservedWords.contains(modified)) {
            return escapeReservedWord(modified)
        }
        // NOTE: another sanitize because camelize can create an invalid name
        return sanitizeKotlinSpecificNames(modified)
    }

    /**
     * Sanitize against Kotlin specific naming conventions, which may differ from those required by [DefaultCodegen.sanitizeName].
     *
     * @param name string to be sanitized
     * @return sanitized string
     */
    private fun sanitizeKotlinSpecificNames(name: String): String {
        var word = name
        for (specialCharacters in specialCharReplacements.entries) {
            word = replaceSpecialCharacters(word, specialCharacters)
        }

        // Fallback, replace unknowns with underscore.
        word = Pattern.compile("\\W+", Pattern.UNICODE_CHARACTER_CLASS).matcher(word).replaceAll("_")
        if (word.matches("\\d.*".toRegex())) {
            word = "_$word"
        }

        // _, __, and ___ are reserved in Kotlin. Treat all names with only underscores consistently, regardless of count.
        if (word.matches("^_*$".toRegex())) {
            word = word.replace("\\Q_\\E".toRegex(), "Underscore")
        }

        return word
    }

    private fun replaceSpecialCharacters(word: String, specialCharacters: MutableMap.MutableEntry<String, String?>): String {
        val specialChar = specialCharacters.key
        val replacementChar = specialCharacters.value
        // Underscore is the only special character we'll allow
        if (specialChar != "_" && word.contains(specialChar)) {
            return replaceCharacters(word, specialChar, replacementChar).toString()
        }
        return word
    }

    private fun replaceCharacters(word: String, oldValue: String, newValue: String?): String? {
        if (!word.contains(oldValue)) {
            return word
        }
        if (word == oldValue) {
            return newValue
        }
        val i = word.indexOf(oldValue)
        val start = word.substring(0, i)
        val end: String? = recurseOnEndOfWord(word, oldValue, newValue, i)
        return start + newValue + end
    }

    private fun recurseOnEndOfWord(word: String, oldValue: String?, newValue: String?, lastReplacedValue: Int): String? {
        var end: String? = word.substring(lastReplacedValue + 1)
        if (!end!!.isEmpty()) {
            end = titleCase(end)
            end = replaceCharacters(end!!, oldValue!!, newValue)
        }
        return end
    }

    private fun titleCase(input: String): String {
        return input.substring(0, 1).uppercase() + input.substring(1)
    }

    override fun toEnumName(property: CodegenProperty): String? {
        return property.nameInPascalCase
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