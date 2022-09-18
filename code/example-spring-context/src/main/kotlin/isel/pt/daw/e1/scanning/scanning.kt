package isel.pt.daw.e1.scanning

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.stereotype.Component
import org.springframework.beans.factory.getBean

private val log = LoggerFactory.getLogger("main")

interface InterfaceA

@Component
class ComponentA : InterfaceA

@Component
data class ComponentB(
    val dependency: InterfaceA
)

fun main() {
    log.info("started")
    // Create the context
    val context = AnnotationConfigApplicationContext()
    // Scan a package for all @Component annotated classes
    context.scan("isel.pt.daw.e1.scanning")
    // Refresh the context to take into consideration the new bean definitions
    context.refresh()
    // Get a bean
    val componentB = context.getBean<ComponentB>()
    log.info("componentB - {}", componentB)
    /*
     * Conclusions:
     * - In this example the bean definitions are not added explicitly to the context.
     * - Instead, the `isel.pt.daw.e1.scanning` package is scanned for classes that have the `@Component` annotation.
     * - The end result is similar to explicitly registering the `ComponentA` and `ComponentB` classes.
     *
     * Experiments:
     * - Remove the call to `context.scan` and run the example.
     * - Remote the `@Component` annotation on one of the classes and run the example.
     */
}
