package pt.isel.daw.tictactow.http

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.web.reactive.server.WebTestClient
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers
import kotlin.test.assertEquals

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ExampleTests {

    // One of the very few places where we use property injection
    @LocalServerPort
    var port: Int = 0

    @Test
    fun exampleTestUsingWebTestClient() {
        val client = WebTestClient.bindToServer().baseUrl("http://localhost:$port").build()
        client.get().uri("/")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("links[0].rel").isEqualTo("self")
    }

    @Test
    fun exampleUsingHttpClient() {
        val client = HttpClient.newHttpClient()
        val response = client.send(
            HttpRequest
                .newBuilder()
                .uri(URI("http://localhost:$port"))
                .GET()
                .build(),
            BodyHandlers.ofString()
        )
        assertEquals(200, response.statusCode())
    }
}