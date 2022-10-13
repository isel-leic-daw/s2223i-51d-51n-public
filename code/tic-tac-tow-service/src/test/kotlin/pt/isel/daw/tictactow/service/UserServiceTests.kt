package pt.isel.daw.tictactow.service

import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import pt.isel.daw.tictactow.Either
import pt.isel.daw.tictactow.domain.UserLogic
import pt.isel.daw.tictactow.utils.Sha256TokenEncoder
import pt.isel.daw.tictactow.utils.testWithTransactionManagerAndRollback
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue
import kotlin.test.fail

class UserServiceTests {

    @Test
    fun `can create user, token, and retrieve by token`(): Unit =
        testWithTransactionManagerAndRollback { transactionManager ->

            // given: a user service
            val userService = UsersService(
                transactionManager,
                UserLogic(),
                BCryptPasswordEncoder(),
                Sha256TokenEncoder(),
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
}