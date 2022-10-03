package pt.isel.daw.tictactow.utils

import pt.isel.daw.tictactow.domain.TokenValidationInfo

interface TokenEncoder {
    fun createValidationInformation(token: String): TokenValidationInfo
    fun validate(validationInfo: TokenValidationInfo, token: String): Boolean
}