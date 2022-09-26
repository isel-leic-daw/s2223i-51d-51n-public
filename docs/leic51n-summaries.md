# LEIC51N Summaries

## Lesson 1 - 2022-09-12

- Course presentation
    - [_Ficha de Unidade Curricular_](https://www.isel.pt/sites/default/files/FUC_LEIC/5sem/DAW_LEIC.pdf)
    - Moodle pages
        - [Course](https://2223moodle.isel.pt/course/view.php?id=6303) ("unidade curricular")
        - [Course section](https://2223moodle.isel.pt/course/view.php?id=6307) ("turma")
    - [Github repository](https://github.com/isel-leic-daw/s2223i-51d-51n-public)
        - [docs](https://github.com/isel-leic-daw/s2223i-51d-51n-public/tree/main/docs)
            - [calendar](https://github.com/isel-leic-daw/s2223i-51d-51n-public/blob/main/docs/calendar.md)
            - [evaluation](https://github.com/isel-leic-daw/s2223i-51d-51n-public/blob/main/docs/evaluation.md)
            - [resource](https://github.com/isel-leic-daw/s2223i-51d-51n-public/blob/main/docs/resources.md)

## Lesson 2 - 2022-09-15

- Inversion of Control, Dependency Injection and the Spring Context library
    - Class stereotypes.
        - Containers of functionality (components) vs containers of data.
    - Dependency relations between _dependents_ and a _ dependencies_.
        - Instance of component `A` requires a instance of component `B` to operate, `A` depends on `B`.
    - Inversion of Control (IoC).
        - `A` does not create instances of `B`. Instead, `A` receives (is injected with) instances of `B`, created outside the `A` control.
    - Dependency Injection (DI).
        - The mechanism by which a dependent receives its dependencies.
        - **Constructor injection** - dependencies are passed in as constructor arguments and stored on immutable fields.
            - The preferable way of dependency injection.
        - **Field or property injection** - dependencies are passed in **after** object creation, via the assignment of fields or the executions of property setter methods.
            - Requires mutable dependency fields.
            - Objects are not ready to operate after construction.
            - Use only when constructor injection is not possible.
    - Object composition.
        - Components instance and their dependency relations define a graph.
        - Creating this graph can be done by:
            - Manually defined code.
            - Automatically, by using a DI container.
    - Spring Context is a DI container.
        - Given a set of **type** information, including their dependencies, it creates the graph of component **instances**.
        - See the `example-spring-context` project on the course repository.
    - Container independence.
        - The component design should be independent of the the container usage.
     - Spring Framework documentation
        - [https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#spring-core]
        - [https://docs.spring.io/spring-framework/docs/current/reference/html/core.html#beans-annotation-config]

- The Servlet specification.
    - Allow request handlers (servlets) and intermediaries (filters) to run on multiple web servers, the so called servlet containers.
    - [Jakarta Specifications](https://jakarta.ee/specifications/servlet/).
    - Servlet
        - Final handler for an HTTP request.
        - Methods that receive
            - An `HttpServletRequest` representing the request information.
            - An `HttpServletResponse` with the response information produced so far.
    - Filter
        - Intermediary that can perform:
            - Pre-processing, before the request-response pair is passed in to the next intermediary or final handler.
            - Post-processinf, after the next intermediary or final handler returned
            - Short-circuit the request processing, by not calling the next intermediary or final handler.
    - See the `example-servlets` project on the course repository.

- Spring Boot
    - [Spring Initializr](https://start.spring.io)
        - [Example](https://start.spring.io/#!type=gradle-project&language=kotlin&platformVersion=2.7.3&packaging=jar&jvmVersion=17&groupId=com.example&artifactId=demo&name=demo&description=Demo%20project%20for%20Spring%20Boot&packageName=com.example.demo&dependencies=web)
    - Analysis of the project generated by [Spring Initializr](https://start.spring.io)
        - Typical Gradle-based JVM project.
        - Included libraries - Spring, Jackson, and Kotlin.
        - Included gradle plugins.
        - `main` function and the `@SpringBootApplication` _application class_.
    - Adding HTTP request handling.
        - _handler_ - method that procudes _response information_ based on _request information_
            - Typically defined as an instance function (i.e. method) **without** a predefined type signature.
            - With additional metadata provided as annotations, to associate it to HTTP request methods and URI path templates.
        - _controller_ - a container of handlers.

## Lesson 3 - 2022-09-19

- Spring MVC (Model-View-Controller)
    - [Documentation](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#spring-web)
    - Servlet-based architecture
        - Single [`DispatcherServlet`](https://docs.spring.io/spring-framework/docs/current/reference/html/web.html#mvc-servlet) mapped to all paths.
        - Servlet container initialization and servlet mapping is done by Spring Boot.
        - This pattern is sometimes named [_front controller_](https://www.martinfowler.com/eaaCatalog/frontController.html).
        - `HttpServletRequest` and `HttpServletResponse` are the base for all request and response interaction.
        - It is possible to use filters (defined according to the servlet interface).
    - Argument resolution (see `ArgumentResolutionExamplesController`).
        - From path template variables.
        - From request parameters (i.e. query string parameters).
        - From the request body using model binding.
        - From the servlet request and response objects.
        - Using custom argument resolvers.
            - See `ClientIpExampleArgumentResolver`.

## Lesson 3 - 2022-09-22
 
- Spring MVC (cont.)
    - Response message production (see `MessageConversionExamplesController`)
        - From primitive types.
        - Using `ResponseEntity` to control other parts of the message, such as the status code and the headers.
        - From complex types, using messsage converts
            - Jackson-based message converter.
            - Creating custom message converters (see `UriToQrCodeMessageConverter`).
    - Configuration via the `WebMvcConfigurer` interface.
    - Intercepting requests via filters and handler interceptors.
        - Differences on the programming models and capabilities.
        - The `HttpServletRequest` attributes bag.

- Introduction to HTTP API design
    - Characterization of APIs
        - On one end
            - Single client.
            - High coordination between client and HTTP API.
                - Easy to add and coordinate changes on both the client and the API.
            - Short-lived: months.
            - Data-oriented.
                - Provides data for clients to use on their own ways
        - On the other end (not really the end)
            - Multiple clients.
            - Dificult to coordinate changes on both the clients and the APIs
            - Long-lived: years, decades.
            - Journey-oriented (process-oriented).
                - Exposes functionality for clients to provide very well defined journeys/processes
    - Introduction to the use of hypermedia as a way to reduce coupling between the client and a specific HTTP API implementation.
        - Analysis of a concrete HTTP API.
