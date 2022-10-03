package com.example.demo

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class SingletonScopedComponent(
    val requestScopedComponent: RequestScopedComponent
) {
    init {
        logger.info("ctor")
    }

    companion object {
        private val logger = LoggerFactory.getLogger(SingletonScopedComponent::class.java)
    }
}
