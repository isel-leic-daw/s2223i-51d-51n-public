package pt.isel.daw.tictactow.service

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import pt.isel.daw.tictactow.Either
import pt.isel.daw.tictactow.domain.UserLogic
import pt.isel.daw.tictactow.utils.Sha256TokenEncoder
import pt.isel.daw.tictactow.utils.TestClock
import pt.isel.daw.tictactow.utils.testWithTransactionManagerAndRollback
import java.time.Duration
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue
import kotlin.test.fail

class UserServiceTests {

    @Test
    fun `can create user, token, and retrieve by token`(): Unit =
        testWithTransactionManagerAndRollback { transactionManager ->

            // given: a user service
            val testClock = TestClock()
            val userService = UsersService(
                transactionManager,
                UserLogic(),
                BCryptPasswordEncoder(),
                Sha256TokenEncoder(),
                testClock
            )

            // when: creating a user
            val createUserResult = userService.createUser("bob", "changeit")

            // then: the creation is successful
            when (createUserResult) {
                is Either.Left -> fail("Unexpected $createUserResult")
                is Either.Right -> assertTrue(createUserResult.value.length > 0)
            }

            // when: creating a token
            val createTokenResult = userService.createToken("bob", "changeit")

            // then: the creation is successful
            val token = when (createTokenResult) {
                is Either.Left -> Assertions.fail(createTokenResult.toString())
                is Either.Right -> createTokenResult.value
            }

            // and: the token bytes have the expected length
            val tokenBytes = Base64.getUrlDecoder().decode(token)
            assertEquals(256 / 8, tokenBytes.size)

            // when: retrieving the user by token
            val user = userService.getUserByToken(token)

            // then: a user is found
            assertNotNull(user)

            // and: has the expected name
            assertEquals("bob", user.username)
        }

    @Test
    fun `can use token during rolling period but not after absolute TTL`() {
        testWithTransactionManagerAndRollback { transactionManager ->

            // given: a user service
            val testClock = TestClock()
            val userService = UsersService(
                transactionManager,
                UserLogic(),
                BCryptPasswordEncoder(),
                Sha256TokenEncoder(),
                testClock
            )

            // when: creating a user
            val createUserResult = userService.createUser("bob", "changeit")

            // then: the creation is successful
            when (createUserResult) {
                is Either.Left -> fail("Unexpected $createUserResult")
                is Either.Right -> assertTrue(createUserResult.value.length > 0)
            }

            // when: creating a token
            val createTokenResult = userService.createToken("bob", "changeit")

            // then: the creation is successful
            val token = when (createTokenResult) {
                is Either.Left -> Assertions.fail(createTokenResult.toString())
                is Either.Right -> createTokenResult.value
            }

            // when: retrieving the user after (rolling TTL - 1s) intervals
            val startInstant = testClock.now()
            while (true) {
                testClock.advance(UsersService.TOKEN_ROLLING_TTL.minusSeconds(1))
                userService.getUserByToken(token) ?: break
            }

            // then: user is not found only after the absolute TTL has elapsed
            assertTrue(Duration.between(startInstant, testClock.now()) > UsersService.TOKEN_TTL)
        }
    }

    @Test
    fun `can limit the number of tokens`() {
        testWithTransactionManagerAndRollback { transactionManager ->
            // given: a user service
            val testClock = TestClock()
            val userService = UsersService(
                transactionManager,
                UserLogic(),
                BCryptPasswordEncoder(),
                Sha256TokenEncoder(),
                testClock
            )

            // when: creating a user
            val createUserResult = userService.createUser("bob", "changeit")

            // then: the creation is successful
            when (createUserResult) {
                is Either.Left -> fail("Unexpected $createUserResult")
                is Either.Right -> assertTrue(createUserResult.value.length > 0)
            }

            // when: creating MAX tokens
            val tokens = (0 until UsersService.MAX_TOKENS).map {
                val createTokenResult = userService.createToken("bob", "changeit")
                testClock.advance(Duration.ofMinutes(1))

                // then: the creation is successful
                val token = when (createTokenResult) {
                    is Either.Left -> Assertions.fail(createTokenResult.toString())
                    is Either.Right -> createTokenResult.value
                }
                token
            }.toTypedArray().reversedArray()

            // and: using the tokens at different times
            (tokens.indices).forEach {
                assertNotNull(userService.getUserByToken(tokens[it]), "token $it must be valid")
                testClock.advance(Duration.ofSeconds(1))
            }

            // and: creating a new token
            val createTokenResult = userService.createToken("bob", "changeit")
            testClock.advance(Duration.ofMinutes(1))
            val newToken = when (createTokenResult) {
                is Either.Left -> Assertions.fail(createTokenResult.toString())
                is Either.Right -> createTokenResult.value
            }

            // then: newToken is valid
            assertNotNull(userService.getUserByToken(newToken))

            // and: the first token (the least recently used) is not valid
            assertNull(userService.getUserByToken(tokens[0]))

            // and: the remaining tokens are still valid
            (1 until tokens.size).forEach {
                assertNotNull(userService.getUserByToken(tokens[it]))
            }
        }
    }

    @Test
    fun `can limit the number of tokens even if multiple tokens are used at the same time`() {
        testWithTransactionManagerAndRollback { transactionManager ->
            // given: a user service
            val testClock = TestClock()
            val userService = UsersService(
                transactionManager,
                UserLogic(),
                BCryptPasswordEncoder(),
                Sha256TokenEncoder(),
                testClock
            )

            // when: creating a user
            val createUserResult = userService.createUser("bob", "changeit")

            // then: the creation is successful
            when (createUserResult) {
                is Either.Left -> fail("Unexpected $createUserResult")
                is Either.Right -> assertTrue(createUserResult.value.length > 0)
            }

            // when: creating MAX tokens
            val tokens = (0 until UsersService.MAX_TOKENS).map {
                val createTokenResult = userService.createToken("bob", "changeit")
                testClock.advance(Duration.ofMinutes(1))

                // then: the creation is successful
                val token = when (createTokenResult) {
                    is Either.Left -> Assertions.fail(createTokenResult.toString())
                    is Either.Right -> createTokenResult.value
                }
                token
            }.toTypedArray().reversedArray()

            // and: using the tokens at the same time
            testClock.advance(Duration.ofSeconds(1))
            (tokens.indices).forEach {
                assertNotNull(userService.getUserByToken(tokens[it]), "token $it must be valid")
            }

            // and: creating a new token
            val createTokenResult = userService.createToken("bob", "changeit")
            testClock.advance(Duration.ofMinutes(1))
            val newToken = when (createTokenResult) {
                is Either.Left -> Assertions.fail(createTokenResult.toString())
                is Either.Right -> createTokenResult.value
            }

            // then: newToken is valid
            assertNotNull(userService.getUserByToken(newToken))

            // and: exactly one of the previous tokens is now not valid
            assertEquals(
                UsersService.MAX_TOKENS - 1,
                tokens.count {
                    userService.getUserByToken(it) != null
                }
            )
        }
    }
}