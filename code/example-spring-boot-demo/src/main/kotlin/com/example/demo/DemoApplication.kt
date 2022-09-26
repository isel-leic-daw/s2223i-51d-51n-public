package com.example.demo

import com.example.demo.pipeline.argumentresolvers.ClientIpExampleArgumentResolver
import com.example.demo.pipeline.handlerinterceptors.ExampleHandlerInterceptor
import com.example.demo.pipeline.messageconverters.CustomOutputModelMessageConverter
import com.example.demo.pipeline.messageconverters.UriToQrCodeMessageConverter
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.http.converter.HttpMessageConverter
import org.springframework.web.method.support.HandlerMethodArgumentResolver
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import org.springframework.web.servlet.function.RouterFunction

@SpringBootApplication
class DemoApplication(
    private val exampleHandlerInterceptor: ExampleHandlerInterceptor,
    private val clientIpArgumentResolver: ClientIpExampleArgumentResolver,
    private val uriToQrCodeMessageConverter: UriToQrCodeMessageConverter,
) : WebMvcConfigurer {

    @Bean
    fun getExampleRoute(): RouterFunction<*> = exampleRouterFunction

    @Bean
    fun getExampleWithDependenciesRoute(
        greetingsService: GreetingsService,
    ): RouterFunction<*> = exampleRouterFunctionWithDependencies(greetingsService)

    override fun addArgumentResolvers(resolvers: MutableList<HandlerMethodArgumentResolver>) {
        resolvers.add(clientIpArgumentResolver)
    }

    override fun configureMessageConverters(converters: MutableList<HttpMessageConverter<*>>) {
        converters.add(0, uriToQrCodeMessageConverter)
        converters.add(0, CustomOutputModelMessageConverter())
    }

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(exampleHandlerInterceptor)
    }

}

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
}
