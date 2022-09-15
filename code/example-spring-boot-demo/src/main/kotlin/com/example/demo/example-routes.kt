package com.example.demo

import org.springframework.http.MediaType
import org.springframework.web.servlet.function.HandlerFunction
import org.springframework.web.servlet.function.RouterFunction
import org.springframework.web.servlet.function.ServerResponse
import org.springframework.web.servlet.function.router

/*
 * "Functional endpoints" types
 * - HandlerFunction<T>: (ServerRequest) -> (T : ServerResponse)
 * - RouterFunction: (ServerRequest) -> Optional<ServerHandler>
 */

val exampleHandler: HandlerFunction<*> = HandlerFunction { request ->
    ServerResponse.ok().body("Hi, this response was produced by an handler")
}

val exampleRouterFunction: RouterFunction<*> = router {
    GET("/0", exampleHandler::handle)
    // or using a training lambda
    GET("/1") {
        ServerResponse.ok().body("Hi, this response was produced by an handler")
    }
    accept(MediaType.TEXT_PLAIN).nest {
        "functional/examples".nest {
            GET("/0") {
                ServerResponse.ok().body("Hi, this response was produced by an handler")
            }
        }
    }
    accept(MediaType.APPLICATION_JSON).nest {
        "functional/examples".nest {
            GET("/0") {
                ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body("Hello from an accept nested route")
            }
            GET("/1") {
                ServerResponse.ok()
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(Greetings("Hello from an accept nested route"))
            }
            GET("/2/{id}") { request ->
                val id = request.pathVariables()["id"] ?: "no id"
                ServerResponse.ok()
                    .body("The id is '${id}'")
            }
        }
    }
}

fun exampleRouterFunctionWithDependencies(
    greetingsService: GreetingsService
    // ... any additional dependencies ...
) = router {
    "functional/dependencies/examples".nest {
        GET("/0") {
            ServerResponse.ok().body(greetingsService.greetings)
        }
    }
}

data class Greetings(
    val message: String
)
