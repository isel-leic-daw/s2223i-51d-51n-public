package pt.isel.daw.tictactow.http

import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.web.reactive.server.WebTestClient
import java.util.*
import java.util.stream.Stream
import kotlin.test.assertTrue

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserTests {

    // One of the very few places where we use property injection
    @LocalServerPort
    var port: Int = 0

    @Test
    fun `can create an user`() {
        // given: an HTTP client
        val client = WebTestClient.bindToServer().baseUrl("http://localhost:$port").build()

        // and: a random user
        val username = UUID.randomUUID().toString()
        val password = "changeit"

        // when: creating an user
        // then: the response is a 201 with a proper Location header
        client.post().uri("/users")
            .bodyValue(
                mapOf(
                    "username" to username,
                    "password" to password
                )
            )
            .exchange()
            .expectStatus().isCreated
            .expectHeader().value("location") {
                assertTrue(it.startsWith("/users/"))
            }
    }

    @Test
    fun `can create an user, obtain a token, and access user home`() {
        // given: an HTTP client
        val client = WebTestClient.bindToServer().baseUrl("http://localhost:$port").build()

        // and: a random user
        val username = UUID.randomUUID().toString()
        val password = "changeit"

        // when: creating an user
        // then: the response is a 201 with a proper Location header
        client.post().uri("/users")
            .bodyValue(
                mapOf(
                    "username" to username,
                    "password" to password
                )
            )
            .exchange()
            .expectStatus().isCreated
            .expectHeader().value("location") {
                assertTrue(it.startsWith("/users/"))
            }

        // when: creating a token
        // then: the response is a 200
        val result = client.post().uri("/users/token")
            .bodyValue(
                mapOf(
                    "username" to username,
                    "password" to password
                )
            )
            .exchange()
            .expectStatus().isOk
            .expectBody(TokenResponse::class.java)
            .returnResult()
            .responseBody!!

        // when: getting the user home with a valid token
        // then: the response is a 200 with the proper representation
        client.get().uri("/me")
            .header("Authorization", "Bearer ${result.token}")
            .exchange()
            .expectStatus().isOk
            .expectBody()
            .jsonPath("username").isEqualTo(username)

        // when: getting the user home with an invalid token
        // then: the response is a 4001 with the proper problem
        client.get().uri("/me")
            .header("Authorization", "Bearer ${result.token}-invalid")
            .exchange()
            .expectStatus().isUnauthorized
            .expectHeader().valueEquals("WWW-Authenticate", "bearer")
    }

    class TokenResponse(
        val token: String
    )

    @Test
    fun `user creation produces an error if user already exists`() {
        // given: an HTTP client
        val client = WebTestClient.bindToServer().baseUrl("http://localhost:$port").build()

        // and: a random user
        val username = UUID.randomUUID().toString()
        val password = "changeit"

        // when: creating an user
        // then: the response is a 201 with a proper Location header
        client.post().uri("/users")
            .bodyValue(
                mapOf(
                    "username" to username,
                    "password" to password
                )
            )
            .exchange()
            .expectStatus().isCreated
            .expectHeader().value("location") {
                assertTrue(it.startsWith("/users/"))
            }

        // when: creating the same user again
        // then: the response is a 400 with the proper tyoe
        client.post().uri("/users")
            .bodyValue(
                mapOf(
                    "username" to username,
                    "password" to password
                )
            )
            .exchange()
            .expectStatus().isBadRequest
            .expectHeader().contentType("application/problem+json")
            .expectBody()
            .jsonPath("type").isEqualTo(
                "https://github.com/isel-leic-daw/s2223i-51d-51n-public/tree/main/code/" +
                    "tic-tac-tow-service/docs/problems/user-already-exists"
            )
    }

    @Test
    fun `user creation produces an error if password is weak`() {
        // given: an HTTP client
        val client = WebTestClient.bindToServer().baseUrl("http://localhost:$port").build()

        // and: a random user
        val username = UUID.randomUUID().toString()
        val password = "-"

        // when: creating a user
        // then: the response is a 400 with the proper type
        client.post().uri("/users")
            .bodyValue(
                mapOf(
                    "username" to username,
                    "password" to password
                )
            )
            .exchange()
            .expectStatus().isBadRequest
            .expectHeader().contentType("application/problem+json")
            .expectBody()
            .jsonPath("type").isEqualTo(
                "https://github.com/isel-leic-daw/s2223i-51d-51n-public/tree/main/code/" +
                    "tic-tac-tow-service/docs/problems/insecure-password"
            )
    }

    @ParameterizedTest
    @MethodSource
    fun `user creation produces an error request content is not valid`(input: Any) {
        // given: an HTTP client
        val client = WebTestClient.bindToServer().baseUrl("http://localhost:$port").build()

        // when: creating a user with invalid data
        // then: the response is a 400 with the proper type
        client.post().uri("/users")
            .bodyValue(
                input
            )
            .exchange()
            .expectStatus().isBadRequest
            .expectHeader().contentType("application/problem+json")
            .expectBody()
            .jsonPath("type").isEqualTo(
                "https://github.com/isel-leic-daw/s2223i-51d-51n-public/tree/main/code/" +
                    "tic-tac-tow-service/docs/problems/invalid-request-content"
            )
    }

    companion object {
        @JvmStatic
        fun `user creation produces an error request content is not valid`(): Stream<Any> =
            Stream.of(
                mapOf<String, Any>(
                    // no username or password
                ),
                mapOf(
                    "username" to "alice"
                    // no password
                ),
                mapOf(
                    "password" to "bad",
                    // no username
                ),
                mapOf(
                    "username" to listOf<String>(),
                    "password" to "changeit",
                    // invalid username type
                ),
            )
    }
}