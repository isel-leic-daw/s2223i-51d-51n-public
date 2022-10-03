package pt.isel.daw.tictactow.domain

data class User(
    val id: Int,
    val username: String,
    val passwordValidation: PasswordValidationInfo,
)