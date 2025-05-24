import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

// https://docs.gradle.org/current/userguide/composite_builds.html#defining_composite_builds
object Versions {
    const val KOTLIN    = "2.1.20"
    const val KTOR      = "3.1.2"
    const val EXPOSED   = "0.60.0"
    const val POSTGRES  = "42.7.5"
    const val H2        = "2.3.232"
    const val LOGBACK   = "1.4.12"
}

plugins {
    kotlin("jvm") version "2.1.10"
    kotlin("plugin.serialization") version "2.1.10"
    id("org.openapi.generator") version "7.13.0"

    application
}

kotlin {
    jvmToolchain(21)

    sourceSets {
        getByName("main").kotlin.srcDirs("${layout.buildDirectory.get()}/generated")
        getByName("main").kotlin.srcDirs("${layout.buildDirectory.get()}/generated_ddd")
    }
}

group = "hu.gecsevar"
version = "1.0.0"

application {
    mainClass = "hu.gecsevar.openapi.app.ApplicationKt"
}

repositories {
    ivy {
        name = "localrepo"
        url = uri(project.file("../../local-repo"))
    }
    mavenCentral()
}

buildscript {
    dependencies {
        classpath("hu.gecsevar:gv-openapi")
    }
}

tasks.create("openApiGenerate" + "Gecsevar", GenerateTask::class.java) {
    generatorName.set("gv-ktor-server")
    inputSpec.set("$rootDir/app/src/main/resources/test_1.yml")
    outputDir.set("${layout.buildDirectory.get()}/generated/")
    apiPackage.set("hu.gecsevar.openapi.app.plugins.api")
    modelPackage.set("hu.gecsevar.openapi.app.database.view")
    additionalProperties.put("dddFormatting", false)

    // Add these properties for 3.1.x support
    additionalProperties.put("useOneOfDiscriminatorLookup", true)
    additionalProperties.put("supportUrlQuery", true)
    additionalProperties.put("openApi31", true)
    additionalProperties.put("skipValidateSpec", true)

    // Enable legacy mode if needed
    additionalProperties.put("legacyDiscriminatorBehavior", false)
    // Specify OpenAPI specification version
    additionalProperties.put("openApiNullable", true)
    additionalProperties.put("generateNullableTypes", true)

    // Optional: Configure validation
    validateSpec.set(false)

}

//tasks.create("openApiGenerate" + "Gecsevar_DDD", GenerateTask::class.java) {
//    generatorName.set("gv-ktor-server")
//    inputSpec.set("$rootDir/app/src/main/resources/test_1.yml")
//    outputDir.set("${layout.buildDirectory.get()}/generated_ddd/")
//    apiPackage.set("hu.gecsevar.openapi.app.plugins.api")
//    modelPackage.set("hu.gecsevar.openapi.app.database.view")
//    additionalProperties.put("dddFormatting", false)
//}
//tasks.create("openApiGenerate" + "Takarnet", GenerateTask::class.java) {
//    generatorName.set("gv-ktor-server")
//    inputSpec.set("$rootDir/app/src/main/resources/openapi.yaml")
//    outputDir.set("${layout.buildDirectory.get()}/generated/")
//    apiPackage.set("hu.eing.takarnet.app.plugins.api")
//    modelPackage.set("hu.eing.takarnet.app.database.view")
//    nameMappings.set(mutableMapOf(
//        "Kerelem_beerkezesenek_modja" to "Kerelem_beerkezesenek_modja",
//        "Benyujto_szemely_tipusa" to "Benyujto_szemely_tipusa",
//        "A_specialis_szemely_adatai" to "A_specialis_szemely_adatai"))
//}

tasks.compileKotlin {
    dependsOn("openApiGenerateGecsevar")
//    dependsOn("openApiGenerateGecsevar_DDD")
//    dependsOn("openApiGenerateTakarnet")
}

afterEvaluate() {
    tasks.named("build") {
    }
    tasks.named("run") {
    }
}

dependencies {
    implementation("hu.gecsevar:gv-openapi")

    implementation("io.ktor:ktor-server-core-jvm:${Versions.KTOR}")
    implementation("io.ktor:ktor-server-auth-jvm:${Versions.KTOR}")
    implementation("io.ktor:ktor-server-auth-jwt-jvm:${Versions.KTOR}")
    implementation("io.ktor:ktor-serialization-kotlinx-json-jvm:${Versions.KTOR}")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.2")

    testImplementation("io.ktor:ktor-server-test-host:${Versions.KTOR}")
    testImplementation("io.ktor:ktor-client-content-negotiation:${Versions.KTOR}")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:${Versions.KOTLIN}")
    testImplementation("org.jetbrains.kotlin:kotlin-test:${Versions.KOTLIN}")
}

