package isel.pt.daw.e0.first
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.AnnotationConfigApplicationContext

private val log = LoggerFactory.getLogger("main")

interface InterfaceA
class ComponentA : InterfaceA

data class ComponentB(
    val dependency: InterfaceA
)

fun main() {
    log.info("started")
    // Create the context
    val context = AnnotationConfigApplicationContext()
    // Add the bean definitions
    context.register(ComponentA::class.java, ComponentB::class.java)
    // Refresh the context to take into consideration the new bean definitions
    context.refresh()
    // Get a bean
    val componentB = context.getBean(ComponentB::class.java)
    log.info("ComponentB instance -  {}", componentB)
    val interfaceA = context.getBean(InterfaceA::class.java)
    log.info("InterfaceA instance - {}", interfaceA)

    /*
     * Conclusions:
     * - The context needs to provide an instance of ComponentB.
     * - However, the ComponentB constructor needs an instance of InterfaceA.
     * - The context knows how to create an instance of InterfaceA because it knows about ComponentA.
     * - So the context creates an instance of ComponentA and uses it when calling the constructor of ComponentB.
     *   (i.e. the context *injects* the InterfaceA dependency into the ComponentB instance.
     *
     * Questions:
     * - What is the relation between `componentB.dependency` and `interfaceA`?
     *
     * Experiments:
     * - Add an Int argument to ComponentA constructor and observe the execution result.
     * - Add an Int argument to ComponentB constructor and observe the execution result.
     * - Evaluate `context.getBean(ComponentB::class.java)` multiple times and observe the result
     */
}


