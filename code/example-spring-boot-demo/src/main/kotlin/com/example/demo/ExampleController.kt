package com.example.demo

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/examples")
class ExampleController {

    @GetMapping("/0", produces = ["text/main"])
    fun get0() = "Hello world"
}