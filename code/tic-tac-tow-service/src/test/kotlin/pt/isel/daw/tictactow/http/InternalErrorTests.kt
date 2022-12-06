package pt.isel.daw.tictactow.http

import org.hamcrest.CoreMatchers.equalTo
import org.jdbi.v3.core.Jdbi
import org.junit.jupiter.api.Test
import org.postgresql.ds.PGSimpleDataSource
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.test.web.reactive.server.WebTestClient
import pt.isel.daw.tictactow.repository.jdbi.configure
import java.util.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class InternalErrorTests {

    @TestConfiguration
    class TestConfig {
        @Bean
        @Primary
        fun testJdbi() = Jdbi.create(
            PGSimpleDataSource().apply {
                setURL("jdbc:postgresql://bad-host:5432/db?user=dbuser&password=changeit")
            }
        ).configure()
    }

    @LocalServerPort
    var port: Int = 0

    @Test
    fun `Unknown exceptions are mapped into a 500 without a response content`() {
        // given: an HTTP client
        val client = WebTestClient.bindToServer().baseUrl("http://localhost:$port").build()

        // and: a random user
        val username = UUID.randomUUID().toString()
        val password = "is-this-strong?"

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
            .expectStatus().value(equalTo(500))
            .expectHeader().doesNotExist("Content-Type")
    }
}