import org.openapitools.generator.gradle.plugin.tasks.GenerateTask

// https://docs.gradle.org/current/userguide/composite_builds.html#defining_composite_builds
object Versions {
    const val KOTLIN    = "2.1.20"
    const val KTOR      = "3.1.1"
    const val EXPOSED   = "0.60.0"
    const val POSTGRES  = "42.7.5"
    const val H2        = "2.3.232"
    const val LOGBACK   = "1.4.12"
}

plugins {
    kotlin("jvm") version "2.1.10"
    kotlin("plugin.serialization") version "2.1.10"
    id("org.openapi.generator") version "7.11.0"
    application
}

kotlin {
    jvmToolchain(21)

    sourceSets {
        getByName("main").kotlin.srcDirs("${layout.buildDirectory.get()}/generated/hu")
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
    apiPackage.set("hu.gecsevar.plugins.api")
    modelPackage.set("hu.gecsevar.database.view")
}
tasks.compileKotlin {
    dependsOn("openApiGenerateGecsevar")
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

