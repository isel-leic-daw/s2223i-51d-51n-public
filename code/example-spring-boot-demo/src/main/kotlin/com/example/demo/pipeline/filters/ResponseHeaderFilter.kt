package com.example.demo.pipeline.filters

import com.example.demo.RequestScopedComponent
import com.example.demo.pipeline.handlerinterceptors.ExampleHandlerInterceptor
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.io.ByteArrayOutputStream
import java.io.PrintWriter
import javax.servlet.FilterChain
import javax.servlet.ServletOutputStream
import javax.servlet.WriteListener
import javax.servlet.http.HttpFilter
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse
import javax.servlet.http.HttpServletResponseWrapper

@Component
class ResponseHeaderFilter(
    val requestScopedComponent: RequestScopedComponent
) : HttpFilter() {

    companion object {
        private val logger = LoggerFactory.getLogger(ResponseHeaderFilter::class.java)
    }

    override fun doFilter(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        val start = System.nanoTime()
        val wrappedResponse = ResponseWrapper(response)
        requestScopedComponent.path = request.requestURI
        try {
            chain.doFilter(request, wrappedResponse)
        } finally {
            val delta = System.nanoTime() - start
            val handler = request.getAttribute(ExampleHandlerInterceptor.KEY) ?: "<unknown>"

            logger.info("Elapsed time was {} us, handler='{}'", delta / 1000, handler)
            response.addHeader("Elapse-Time", (delta/1000).toString())
            wrappedResponse.flushToWrappedResponse()
        }
    }


    private class StreamWrapper : ServletOutputStream() {

        val outStream = ByteArrayOutputStream()

        override fun write(b: Int) {
            outStream.write(b)
        }

        override fun isReady(): Boolean = true
        override fun setWriteListener(listener: WriteListener?) = throw IllegalStateException()

        fun reset() {
            outStream.reset()
        }

    }

    private class ResponseWrapper(private val response: HttpServletResponse) : HttpServletResponseWrapper(response) {

        private val outStream = StreamWrapper()
        private val writer = PrintWriter(outStream)
        private var error: Int? = null

        override fun getOutputStream(): ServletOutputStream {
            return outStream
        }

        override fun getWriter(): PrintWriter {
            return writer
        }

        override fun flushBuffer() {
            // ignore
        }

        override fun sendError(sc: Int) {
            error = sc
        }

        fun flushToWrappedResponse() {
            writer.flush()
            outStream.flush()
            val bytes = outStream.outStream.toByteArray()
            response.outputStream.write(bytes, 0, bytes.size)
            val observedError = error
            if (observedError != null) {
                super.sendError(observedError)
            }
            response.flushBuffer()
        }
    }
}