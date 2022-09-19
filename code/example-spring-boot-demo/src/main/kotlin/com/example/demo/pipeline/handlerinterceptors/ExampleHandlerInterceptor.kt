package com.example.demo.pipeline.handlerinterceptors

import org.springframework.stereotype.Component
import org.springframework.web.method.HandlerMethod
import org.springframework.web.servlet.HandlerInterceptor
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class ExampleHandlerInterceptor : HandlerInterceptor {

    override fun preHandle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        handler: Any): Boolean {

        if(handler is HandlerMethod) {
            request.setAttribute(KEY, handler.method.name)
        }

        return true
    }

    companion object {
        const val KEY = "ExampleHandlerInterceptorKey"
    }
}