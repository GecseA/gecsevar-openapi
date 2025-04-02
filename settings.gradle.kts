rootProject.name = "gecsevar-openapi"

include("app")

file("modules").listFiles()?.forEach { moduleBuild: File ->
    includeBuild(moduleBuild)
}
