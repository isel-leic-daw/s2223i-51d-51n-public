package pt.isel.daw.tictactow.repository.jdbi

import org.jdbi.v3.core.Handle
import pt.isel.daw.tictactow.repository.GamesRepository
import pt.isel.daw.tictactow.repository.Transaction
import pt.isel.daw.tictactow.repository.UsersRepository

class JdbiTransaction(
    private val handle: Handle
) : Transaction {

    override val usersRepository: UsersRepository by lazy { JdbiUsersRepository(handle) }

    override val gamesRepository: GamesRepository by lazy { JdbiGamesRepository(handle) }

    override fun rollback() {
        handle.rollback()
    }
}