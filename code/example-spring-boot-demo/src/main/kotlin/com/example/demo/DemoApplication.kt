package com.example.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.servlet.function.RouterFunction

@SpringBootApplication
class DemoApplication {

	@Bean
	fun getExampleRoute(): RouterFunction<*> = exampleRouterFunction

	@Bean
	fun getExampleWithDependenciesRoute(
		greetingsService: GreetingsService,
	): RouterFunction<*> = exampleRouterFunctionWithDependencies(greetingsService)
}

fun main(args: Array<String>) {
	runApplication<DemoApplication>(*args)
}
