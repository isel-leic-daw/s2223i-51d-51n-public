package isel.pt.daw.e3.router

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.stereotype.Component

private val log = LoggerFactory.getLogger("main")

/**
 * An annotation to mark all handler methods
 * (which don't need to have a specific signature)
 */
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
annotation class Handler(
    val method: String,
    val pathTemplate: String,
)

/**
 * Marker interface for all controllers, i.e., for all classes containing handler methods.
 */
interface Controller

@Component
class SomeController : Controller {

    @Handler("GET", "some/path")
    fun getIndex(): String {
        return "Hello"
    }
}

@Component
class AnotherController : Controller {

    @Handler("GET", "another/path")
    fun getIndex(): String {
        return "Hello"
    }

    @Handler("POST", pathTemplate="another/path")
    fun create(): Unit {
        // something...
    }
}

@Component
class NotAController

@Component
class Router(
    val controllers: List<Controller>
)

private fun main() {

    log.info("started")
    // Create the context
    val context = AnnotationConfigApplicationContext()
    // Scan a package for all @Component annotated classes
    context.scan("isel.pt.daw.e3.router")
    // Refresh the context to take into consideration the new bean definitions
    context.refresh()
    // Get the router
    val router = context.getBean(Router::class.java)

    log.info("Available controllers - {}", router.controllers)
    router.controllers.forEach {
        val handlerMethods = it.javaClass.methods.filter {method ->
            method.isAnnotationPresent(Handler::class.java)
        }
        handlerMethods.forEach { method ->
            val annotation = method.getAnnotation(Handler::class.java)
            log.info("'{}' on '{}' is handled by '{}'", annotation.method, annotation.pathTemplate, method)
        }
    }
}
