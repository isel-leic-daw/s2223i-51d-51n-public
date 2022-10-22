package pt.isel.daw.tictactow.http.model

data class StatusOutputModel(
    val hostname: String,
    val gamesCount: Int,
)