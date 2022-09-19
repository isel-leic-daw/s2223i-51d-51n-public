package com.example.demo.pipeline.filters

import com.example.demo.pipeline.handlerinterceptors.ExampleHandlerInterceptor
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
class ResponseHeaderFilter : HttpFilter() {

    override fun doFilter(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {

        val wrapper = ResponseWrapper(response)
        val start = System.nanoTime()
        chain.doFilter(request, wrapper)
        request.getAttribute(ExampleHandlerInterceptor.KEY)?.let {
            wrapper.addHeader("Spring-Method", it.toString())
        }
        val delta = System.nanoTime() - start
        wrapper.addHeader("Test-Header", delta.toString())
        wrapper.flushToWrappedResponse()
    }


    private class StreamWrapper : ServletOutputStream() {

        val outStream = ByteArrayOutputStream()

        override fun write(b: Int) {
            outStream.write(b)
        }

        override fun isReady(): Boolean = true
        override fun setWriteListener(listener: WriteListener?) = throw IllegalStateException()

    }

    private class ResponseWrapper(private val response: HttpServletResponse) : HttpServletResponseWrapper(response) {

        private val outStream = StreamWrapper()
        private val writer = PrintWriter(outStream)

        override fun getOutputStream(): ServletOutputStream {
            return outStream
        }

        override fun getWriter(): PrintWriter {
            return writer
        }

        fun flushToWrappedResponse() {
            writer.flush()
            outStream.flush()
            val bytes = outStream.outStream.toByteArray()
            response.outputStream.write(bytes, 0, bytes.size)
        }
    }
}