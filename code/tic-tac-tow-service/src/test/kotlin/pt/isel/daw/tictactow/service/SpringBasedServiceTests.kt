package pt.isel.daw.tictactow.service

import org.junit.jupiter.api.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import pt.isel.daw.tictactow.domain.PasswordValidationInfo
import pt.isel.daw.tictactow.domain.Token
import pt.isel.daw.tictactow.domain.TokenValidationInfo
import pt.isel.daw.tictactow.domain.User
import pt.isel.daw.tictactow.domain.UserLogic
import pt.isel.daw.tictactow.repository.Transaction
import pt.isel.daw.tictactow.repository.TransactionManager
import pt.isel.daw.tictactow.repository.UsersRepository
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@SpringBootTest
class SpringBasedServiceTests {

    @Autowired
    lateinit var usersService: UsersService

    @Autowired
    lateinit var userLogic: UserLogic

    @TestConfiguration
    class Config {
        @Bean
        fun transactionManager(): TransactionManager = object : TransactionManager {
            override fun <R> run(block: (Transaction) -> R): R {
                val mockedTransaction = mock<Transaction> {
                    val mockedUsersRepository = mock<UsersRepository> {
                        on { getTokenByTokenValidationInfo(any()) } doReturn Pair(
                            User(1, "alice", PasswordValidationInfo("")),
                            Token(TokenValidationInfo(""), 1, Instant.now(), Instant.now())
                        )
                    }

                    on { usersRepository } doReturn mockedUsersRepository
                }

                return block(mockedTransaction)
            }
        }
    }

    @Test
    fun check() {
        // given: a valid token
        val token = userLogic.generateToken()

        // when: getting a user given a token
        val user = usersService.getUserByToken(token)

        // then: a user is returned
        assertNotNull(user)
        assertEquals("alice", user.username)
    }
}