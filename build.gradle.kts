val ossrhUsername: String? by project
val ossrhPassword: String? by project

plugins {
    kotlin("jvm") version "1.8.0"
    id("com.github.johnrengelman.shadow") version "7.0.0"
    `maven-publish`
    signing
}

group = "hu.gecsevar"
version = "1.0.15"

repositories {
    mavenCentral()
}

dependencies {
    compileOnly("org.openapitools", "openapi-generator", "6.5.0")
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
            username = ossrhUsername
            password = ossrhPassword
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
