package hu.gecsevar.openapi.app.plugins

import hu.gecsevar.openapi.app.plugins.api.Test1ApiImpl
import hu.gecsevar.openapi.app.plugins.api.test1Api
import io.ktor.server.application.*
import io.ktor.server.routing.*

fun Application.configureRouting() {

    routing {
        test1Api(Test1ApiImpl())
    }
}