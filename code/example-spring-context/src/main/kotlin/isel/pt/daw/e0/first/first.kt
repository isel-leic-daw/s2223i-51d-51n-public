package isel.pt.daw.e0.first

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.beans.factory.getBean

private val log = LoggerFactory.getLogger("main")

// Interface with some functionality.
// This is the dependency
interface InterfaceA
interface InterfaceB

// An implementation of that interface.
class ComponentA : InterfaceA{
    init {
        log.info("ComponentA ctor")
    }
}

// A dependent on the dependency
// Constructor Injection
class ComponentB(
    val dependency: InterfaceA
) : InterfaceB {
    init {
        log.info("ComponentB ctor")
    }
}

class ComponentC(
    val dependency: InterfaceB
){
    init {
        log.info("ComponentC ctor")
    }
}

fun main() {
    log.info("started")
    // Create the context
    val context = AnnotationConfigApplicationContext()
    // Add the bean definitions
    context.register(
        ComponentC::class.java,
        ComponentA::class.java,
        ComponentB::class.java,
    )
    // Refresh the context to take into consideration the new bean definitions
    log.info("Calling refresh")
    context.refresh()
    // Get a bean
    log.info("Getting beans")
    val componentC = context.getBean<ComponentC>()
    log.info("ComponentC instance - {}", componentC)
    val anotherComponentC = context.getBean<ComponentC>()
    val componentB = context.getBean<ComponentB>()

    log.info("componentC - {}, anotherComponentC - {}", componentC, anotherComponentC)
    log.info("componentC.dependency - {}, componentB - {}", componentC.dependency, componentB)
    //val componentC = context.getBean(ComponentC::class.java)

    /*
    val componentB = context.getBean(ComponentB::class.java)
    log.info("ComponentB instance -  {}", componentB)
    val interfaceA = context.getBean(InterfaceA::class.java)
    log.info("InterfaceA instance - {}", interfaceA)
    log.info("InterfaceA inside ComponentB - {}", componentB.dependency)

    val componentC = context.getBean(ComponentC::class.java)
    log.info("componentC instance - {}", componentC)
    */
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


