package pt.isel.daw.tictactow.http.pipeline

import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import pt.isel.daw.tictactow.domain.User
import pt.isel.daw.tictactow.service.UsersService

@Component
class AuthorizationHeaderProcessor(
    val usersService: UsersService
) {

    fun process(authorizationValue: String?): User? {
        if (authorizationValue == null) {
            return null
        }
        val parts = authorizationValue.trim().split(" ")
        if (parts.size != 2) {
            return null
        }
        if (parts[0].lowercase() != SCHEME) {
            return null
        }
        return usersService.getUserByToken(parts[1])
    }

    companion object {
        private val logger = LoggerFactory.getLogger(AuthorizationHeaderProcessor::class.java)
        const val SCHEME = "bearer"
    }
}