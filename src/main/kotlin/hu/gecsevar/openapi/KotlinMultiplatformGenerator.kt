package hu.gecsevar.openapi

import org.openapitools.codegen.CodegenConfig
import org.openapitools.codegen.CodegenType
import org.openapitools.codegen.DefaultCodegen
import org.openapitools.codegen.model.ModelMap
import org.openapitools.codegen.model.OperationsMap
import java.io.File
import java.util.*


class KotlinMultiplatformGenerator : DefaultCodegen(), CodegenConfig {

  init {
    outputFolder = "generated/"
    modelTemplateFiles["model.mustache"] = ".kt"
    apiTemplateFiles["api.mustache"] = ".kt"
    templateDir = "gv-kotlin-multiplatform"
    apiPackage = "org.openapitools.api"
    modelPackage = "org.openapitools.model"

    reservedWords = HashSet<String> (
      Arrays.asList(
        "sample1",  // replace with static values
        "sample2")
    )

    //additionalProperties.put("apiVersion", apiVersion);

    //supportingFiles.add(SupportingFile("myFile.mustache","", "myFile.sample"))

    languageSpecificPrimitives = HashSet<String>(
      Arrays.asList(
        "Type1",      // replace these with your types
        "Type2")
    )
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
    typeMapping["array"] = "kotlin.Array"
    typeMapping["list"] = "kotlin.Array"
    typeMapping["map"] = "kotlin.collections.Map"
    typeMapping["object"] = "kotlin.Any"
    typeMapping["binary"] = "kotlin.Array<kotlin.Byte>"
    typeMapping["Date"] = "java.time.LocalDateTime"
    typeMapping["DateTime"] = "Instant" // "java.time.LocalDateTime"
    typeMapping["ByteArray"] = "kotlin.ByteArray"

    instantiationTypes["array"] = "arrayOf"
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
  }
  // source folder where to write the files
  protected val sourceFolder = "src"
  val apiVersion = "1.0.5"

  override fun getTag(): CodegenType {
    return CodegenType.SERVER
  }

  override fun getName(): String {
    return "gv-kotlin-multiplatform"
  }

  override fun getHelp(): String {
    return "Bakfitty"
  }

  override fun apiFileFolder(): String {
    return outputFolder + "/" + sourceFolder + "/" + apiPackage().replace('.', File.separatorChar)
  }

  override fun modelFileFolder(): String {
    return outputFolder + "/" + sourceFolder + "/" + modelPackage().replace('.', File.separatorChar)
  }

  override fun postProcessOperationsWithModels(objs: OperationsMap?, allModels: MutableList<ModelMap>?): OperationsMap {
    // to try debugging your code generator:
    // set a break point on the next line.
    // then debug the JUnit test called LaunchGeneratorInDebugger

    val results = super.postProcessOperationsWithModels(objs, allModels);

    val ops = results.operations;
    val opList = ops.operation;

    // iterate over the operation and perhaps modify something
    opList.forEach {
      // example:
      // co.httpMethod = co.httpMethod.toLowerCase();
    }

    return results;
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
    return "_$name"
  }
}
