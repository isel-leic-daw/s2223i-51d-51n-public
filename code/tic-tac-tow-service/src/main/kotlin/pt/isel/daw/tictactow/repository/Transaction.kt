package pt.isel.daw.tictactow.repository

interface Transaction {

    val usersRepository: UsersRepository

    val gamesRepository: GamesRepository

    fun rollback()
}
