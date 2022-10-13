package pt.isel.daw.tictactow.http.model

data class UserCreateInputModel(
    val username: String,
    val password: String,
)

data class UserCreateTokenInputModel(
    val username: String,
    val password: String,
)