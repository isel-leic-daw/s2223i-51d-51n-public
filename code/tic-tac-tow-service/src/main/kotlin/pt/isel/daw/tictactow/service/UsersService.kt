package pt.isel.daw.tictactow.service

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import pt.isel.daw.tictactow.Clock
import pt.isel.daw.tictactow.Either
import pt.isel.daw.tictactow.domain.PasswordValidationInfo
import pt.isel.daw.tictactow.domain.Token
import pt.isel.daw.tictactow.domain.User
import pt.isel.daw.tictactow.domain.UserLogic
import pt.isel.daw.tictactow.repository.TransactionManager
import pt.isel.daw.tictactow.utils.TokenEncoder
import java.time.Duration

sealed class UserCreationError {
    object UserAlreadyExists : UserCreationError()
    object InsecurePassword : UserCreationError()
}
typealias UserCreationResult = Either<UserCreationError, String>

sealed class TokenCreationError {
    object UserOrPasswordAreInvalid : TokenCreationError()
}
typealias TokenCreationResult = Either<TokenCreationError, String>

@Component
class UsersService(
    private val transactionManager: TransactionManager,
    private val userLogic: UserLogic,
    private val passwordEncoder: PasswordEncoder,
    private val tokenEncoder: TokenEncoder,
    private val clock: Clock,
) {

    fun createUser(username: String, password: String): UserCreationResult {
        if (!userLogic.isSafePassword(password)) {
            return Either.Left(UserCreationError.InsecurePassword)
        }

        val passwordValidationInfo = PasswordValidationInfo(
            passwordEncoder.encode(password)
        )

        return transactionManager.run {
            val usersRepository = it.usersRepository
            if (usersRepository.isUserStoredByUsername(username)) {
                Either.Left(UserCreationError.UserAlreadyExists)
            } else {
                val id = usersRepository.storeUser(username, passwordValidationInfo)
                Either.Right(id)
            }
        }
    }

    fun createToken(username: String, password: String): TokenCreationResult {
        if (username.isBlank() || password.isBlank()) {
            Either.Left(TokenCreationError.UserOrPasswordAreInvalid)
        }
        return transactionManager.run {
            val usersRepository = it.usersRepository
            val user: User = usersRepository.getUserByUsername(username) ?: return@run userNotFound()
            if (!passwordEncoder.matches(password, user.passwordValidation.validationInfo)) {
                return@run Either.Left(TokenCreationError.UserOrPasswordAreInvalid)
            }
            val token = userLogic.generateToken()
            val now = clock.now()
            val newToken = Token(tokenEncoder.createValidationInformation(token), user.id, now, now)
            usersRepository.createToken(newToken, MAX_TOKENS)
            Either.Right(token)
        }
    }

    fun getUserByToken(token: String): User? {
        if (!userLogic.canBeToken(token)) {
            return null
        }
        return transactionManager.run {
            val usersRepository = it.usersRepository
            val tokenValidationInfo = tokenEncoder.createValidationInformation(token)
            val userAndToken = usersRepository.getTokenByTokenValidationInfo(tokenValidationInfo)
            if (userAndToken != null && isTokenStillValid(userAndToken.second)) {
                usersRepository.updateTokenLastUsed(userAndToken.second, clock.now())
                userAndToken.first
            } else {
                null
            }
        }
    }

    private fun isTokenStillValid(token: Token): Boolean {
        val now = clock.now()
        return now.isBefore(token.createdAt.plus(TOKEN_TTL)) &&
            now.isBefore(token.lastUsedAt.plus(TOKEN_ROLLING_TTL))
    }

    private fun userNotFound(): TokenCreationResult {
        passwordEncoder.encode("changeit")
        return Either.Left(TokenCreationError.UserOrPasswordAreInvalid)
    }

    companion object {
        val TOKEN_ROLLING_TTL: Duration = Duration.ofHours(1)
        val TOKEN_TTL: Duration = Duration.ofDays(1)
        val MAX_TOKENS: Int = 3
    }
}