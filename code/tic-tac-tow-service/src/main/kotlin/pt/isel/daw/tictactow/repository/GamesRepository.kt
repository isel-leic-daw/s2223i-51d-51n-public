package pt.isel.daw.tictactow.repository

import pt.isel.daw.tictactow.domain.Game
import java.util.UUID

interface GamesRepository {
    fun insert(game: Game)
    fun getById(id: UUID): Game?
    fun update(game: Game)
    fun count(): Int
}