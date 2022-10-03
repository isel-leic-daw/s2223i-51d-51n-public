package pt.isel.daw.tictactow.repository

import pt.isel.daw.tictactow.domain.PasswordValidationInfo
import pt.isel.daw.tictactow.domain.TokenValidationInfo
import pt.isel.daw.tictactow.domain.User

interface UsersRepository {

    fun storeUser(
        username: String,
        passwordValidation: PasswordValidationInfo,
    ): Boolean

    fun getUserByUsername(username: String): User?

    fun getUserByTokenValidationInfo(tokenValidationInfo: TokenValidationInfo): User?

    fun isUserStoredByUsername(username: String): Boolean

    fun createToken(userId: Int, token: TokenValidationInfo)
}