package isel.pt.daw

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpFilter
import jakarta.servlet.http.HttpServlet
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * The servlet API (interfaces and base classes) were initially defined by [Java Enterprise Edition]
 * (https://www.oracle.com/java/technologies/java-ee-glance.html).
 * They are now maintained by project [Jakarta](https://projects.eclipse.org/projects/ee4j)
 *
 */
class ExampleServet : HttpServlet() {

    override fun doGet(
        request: HttpServletRequest,
        response: HttpServletResponse,
    ) : Unit {
        log.info("doGet: method='{}', uri='{}'",
            request.method,
            request.requestURI)

        response.status = 200
        // QUESTION what happens if 'charset=utf-8' is removed
        response.addHeader("Content-Type", "text/plain; charset=utf-8")
        response.outputStream.apply {
            write("Olá mundo".toByteArray(Charsets.UTF_8))
            flush()
        }
        log.info("doGet: ending")
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(ExampleServet::class.java)
    }
}

class ExampleFilter : HttpFilter() {
    override fun doFilter(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain) {
        val start = System.nanoTime()
        log.info("doFilter: before chain call")
        chain.doFilter(request, response)
        log.info("doFilter: after chain call")
        val end = System.nanoTime()
        val delta = end - start
    }

    companion object {
        private val log: Logger = LoggerFactory.getLogger(ExampleFilter::class.java)
    }
}
