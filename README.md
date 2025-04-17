![my workflow](https://github.com/GecseA/gecsevar-openapi/actions/workflows/master.yml/badge.svg)
[![Maven Central](https://img.shields.io/maven-metadata/v.svg?label=maven-central&metadataUrl=https%3A%2F%2Frepo1.maven.org%2Fmaven2%2Fhu%2Fgecsevar%2Fgv-openapi%2Fmaven-metadata.xml)](http://mvnrepository.com/hu.gecsevar/hu.gecsevar.openapi)

# gecsevar-openapi

Yet Another Custom Open API generator (probably ;)

# Features:

kotlin-ktor-server
  - model
  - api (Route.XXX and interface)

kotlin-multiplatform-client (mobile Android & iOS related)
  - model
  - api (client Http calls)
  
# How to use

Simply add a task to your gradle build config like:
(builds are in the central repo)

```
kotlin {
    jvmToolchain(21)

    sourceSets {
        getByName("main").kotlin.srcDirs("${layout.buildDirectory.get()}/generated/my")
    }
}

buildscript {
    dependencies {
        classpath("hu.gecsevar:gv-openapi")
    }
}

tasks.create("openApiGenerate" + "MyApi", GenerateTask::class.java) {
    generatorName.set("gv-ktor-server")
    inputSpec.set("$rootDir/src/main/resources/my-awesome.yaml")
    outputDir.set("${layout.buildDirectory.get()}/generated/")
    apiPackage.set("my.domain.api")
    modelPackage.set("my.domain.schemas")
    //optional nameMappings.set(mutableMapOf("challenge_ts" to "challenge_ts"))    // _ REPLACED TO underscore IF NOT MAPPED
}

tasks.compileKotlin {
    dependsOn("openApiGenerateMyApi")
}
```
