val ossrhUsername: String? by project
val ossrhPassword: String? by project
val ossindexApiToken: String? by project
val ossrhAccessUserName: String? by project
val ossrhAccessUserToken: String? by project

plugins {
    kotlin("jvm") version "2.2.20"
    kotlin("plugin.serialization") version "2.2.20"
    id("com.gradleup.shadow") version "9.0.0-beta2"
    id("io.github.gradle-nexus.publish-plugin") version "2.0.0"
    id("org.sonarqube") version "7.3.0.8198"
    `java-library`
    `maven-publish`
    signing
}

group = "hu.gecsevar"
version = "3.0.0"

kotlin {
    jvmToolchain(24)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(24))
    }
    withJavadocJar()
    withSourcesJar()
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_24)
    }
}

repositories {
    gradlePluginPortal()
    mavenCentral()
}

dependencies {
//    https://github.com/OpenAPITools/openapi-generator/blob/v7.10.0/modules/openapi-generator/src/main/resources/kotlin-server/api_doc.mustache
    compileOnly("org.openapitools:openapi-generator:${Versions.OPENAPI_GENERATOR}")
    // https://mvnrepository.com/artifact/gg.jte/jte
    implementation("gg.jte:jte:3.1.16")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:${Versions.KOTLINX_SERIALIZATION}")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${Versions.KOTLINX_SERIALIZATION}")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime-jvm:0.7.1")

    testImplementation(kotlin("test"))
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:${Versions.KOTLIN}")
    testImplementation("org.jetbrains.kotlin:kotlin-test:${Versions.KOTLIN}")
    testImplementation("org.openapitools:openapi-generator:${Versions.OPENAPI_GENERATOR}")
}

tasks.test {
    useJUnitPlatform()
}

publishing {
    repositories {
        ivy {
            setUrl(file("../../../local-repo"))
        }
    }
}
// exec:
// .\gradlew.bat :gv-openapi:publishToSonatype :gv-openapi:closeSonatypeStagingRepository
nexusPublishing {
    repositories {
        sonatype {
            username.set(ossrhAccessUserName)
            password.set(ossrhAccessUserToken)
            nexusUrl.set(uri("https://ossrh-staging-api.central.sonatype.com/service/local/"))
            snapshotRepositoryUrl.set(uri("https://central.sonatype.com/repository/maven-snapshots/"))
            stagingProfileId.set("hu.gecsevar")
        }
    }
}

publishing {
    publications {
        register("mavenJava", MavenPublication::class.java) {
            from(components["kotlin"])
            artifact(tasks.getByName("kotlinSourcesJar"))
            artifact(tasks.getByName("javadocJar"))
            //artifact(tasks.getByName("sourcesJar"))
            pom {
                name.set(rootProject.name)
                packaging = "jar"
                description.set("OpenAPI code generator in Kotlin for ktor framework." +
                        "It creates interfaces for Client & Server, " +
                        "abstract classes for Routes, you'll need to simple implement, " +
                        "data classes for each schema DTOs. " +
                        "see Limitation.md")
                url.set("https://github.com/GecseA/gecsevar-openapi")
                scm {
                    connection.set("scm:git:git://github.com/GecseA/gecsevar-openapi.git")
                    developerConnection.set("scm:git:ssh://github.com/GecseA/gecsevar-openapi.git")
                    url.set("https://github.com/GecseA/gecsevar-openapi")
                }
                licenses {
                    license {
                        name.set("The Apache License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                    }
                }
                developers {
                    developer {
                        id.set("AstrA")
                        name.set("Attila Gecse")
                        email.set("gecsevar@gmail.com")
                        organization.set("self employed")
                        organizationUrl.set("https://gecsevar.hu")
                    }
                }
            }
        }
    }
}

signing {
    useGpgCmd()
    sign(publishing.publications)
}
