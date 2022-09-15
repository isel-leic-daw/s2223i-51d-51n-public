package com.example.demo

import org.springframework.stereotype.Component

@Component
class DefaultGreetingsService : GreetingsService {
    override val greetings = "Hello"
}