package com.example.demo

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.web.context.annotation.RequestScope


@Component
@RequestScope
class RequestScopedComponent {
    var path: String? = null

    override fun toString() = "${hashCode()} - $path"

    init {
        logger.info("ctor")
    }

    companion object {
        private val logger = LoggerFactory.getLogger(RequestScopedComponent::class.java)
    }
}