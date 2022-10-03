package com.example.demo.controllers

import com.example.demo.SingletonScopedComponent
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("examples-scopes")
class ScopesExampleController(
    private val singletonScopedComponent: SingletonScopedComponent
) {

    @GetMapping("/0/{id}")
    fun get0() = "${singletonScopedComponent.hashCode()}, " +
            "${singletonScopedComponent.requestScopedComponent.toString()}"
}

