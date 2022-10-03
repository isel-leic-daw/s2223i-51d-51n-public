package pt.isel.daw.tictactow.domain

import java.security.SecureRandom
import java.util.*

class UserLogic {

    fun generateToken(): String =
        ByteArray(TOKEN_BYTE_SIZE).let { byteArray ->
            SecureRandom.getInstanceStrong().nextBytes(byteArray)
            Base64.getUrlEncoder().encodeToString(byteArray)
        }

    fun canBeToken(token: String): Boolean = try {
        Base64.getUrlDecoder()
            .decode(token).size == TOKEN_BYTE_SIZE
    } catch (ex: IllegalArgumentException) {
        false
    }

    // TODO it could be better
    fun isSafePassword(password: String) = password.length > 4

    companion object {
        private const val TOKEN_BYTE_SIZE = 256 / 8
    }
}