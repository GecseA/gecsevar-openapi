![my workflow](https://github.com/GecseA/gecsevar-openapi/actions/workflows/gradle.yml/badge.svg)

# gecsevar-openapi

Yet Another Custom Open API generator (probably ;)

# Features:

kotlin-ktor-server
  - model
  - api (Route.XXX and interface)

kotlin-multiplatform-client (mobile Android & iOS related)
  - model
  - api (client Http calls)
  
React/JS (Typescript)
  - model
  - api (client Http calls)

# How to use

Simply add a task to your gradle build config like:

```
buildscript {
    repositories {
        flatDir{dirs ("<>")}
    }
    dependencies {
        classpath(files("<WILL BE IN MAVEN>/hu.gecsevar.openapi-1.0.5-all.jar"))
    }
}

tasks {
    named<org.openapitools.generator.gradle.plugin.tasks.GenerateTask>("openApiGenerate") {
        generatorName.set("gv-ktor-server")
        inputSpec.set("$rootDir/src/main/resources/my-awesome.yaml")
        outputDir.set("$buildDir/generated/")
        apiPackage.set("my.domain.api")
        modelPackage.set("my.domain.schemas")
    }
}

afterEvaluate() {
    tasks.named("build") {
        dependsOn("openApiGenerate")
    }
    tasks.named("run") {
        dependsOn("openApiGenerate")
    }
}
```
