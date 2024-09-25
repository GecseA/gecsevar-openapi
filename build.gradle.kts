val ossrhUsername: String? by project
val ossrhPassword: String? by project
val ossindexApiToken: String? by project
val ossrhAccessUserName: String? by project
val ossrhAccessUserToken: String? by project


plugins {
    kotlin("jvm") version "2.0.10"
    kotlin("plugin.serialization") version "2.0.10"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    `maven-publish`
    signing
}

group = "hu.gecsevar"
version = "1.2.0"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.openapitools", "openapi-generator", "7.8.0")
    // https://mvnrepository.com/artifact/gg.jte/jte
    implementation("gg.jte:jte:3.1.12")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime-jvm:0.6.1")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

java {
    withJavadocJar()
    withSourcesJar()
}

publishing {
    repositories.maven("https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/") {
        name = "OSSRH"
        credentials {
            username = ossrhAccessUserName
            password = ossrhAccessUserToken
        }
    }
    publications {
        register("mavenJava", MavenPublication::class.java) {
            from(components["kotlin"])
            artifact(tasks.getByName("kotlinSourcesJar"))
            artifact(tasks.getByName("javadocJar"))
            pom {
                name.set(rootProject.name)
                packaging = "jar"
                description.set("Yet another OpenAPI code generator - but it tries not to generate the" +
                        " whole world - only interfaces, implementations (client) and view models.")
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

/*
/*
** Github repo is *NOT* public!
*/
publishing {
    publications.create<MavenPublication>("shadow") {
        project.extensions.configure<com.github.jengelman.gradle.plugins.shadow.ShadowExtension> {
            component(this@create)
        }
        groupId = "$group"
        artifactId = "openapi"
        version = "$version"
    }

    repositories.maven("https://maven.pkg.github.com/GecseA/gecsevar-openapi") {
        name = "GitHubPackages"

        credentials {
            username = System.getenv("GITHUB_ACTOR")
            password = System.getenv("GITHUB_TOKEN")
        }
    }
}
*/
