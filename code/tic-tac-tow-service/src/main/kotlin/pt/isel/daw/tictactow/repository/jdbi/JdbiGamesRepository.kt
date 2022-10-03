package pt.isel.daw.tictactow.repository.jdbi

import org.jdbi.v3.core.Handle
import org.jdbi.v3.core.kotlin.mapTo
import org.jdbi.v3.core.mapper.Nested
import pt.isel.daw.tictactow.domain.Board
import pt.isel.daw.tictactow.domain.Game
import pt.isel.daw.tictactow.domain.User
import pt.isel.daw.tictactow.repository.GamesRepository
import java.time.Instant
import java.util.*

class JdbiGamesRepository(
    private val handle: Handle,
) : GamesRepository {

    override fun insert(game: Game) {
        handle.createUpdate(
            """
           insert into dbo.Games(id, state, board, created, updated, deadline, player_x, player_o) 
           values(:id, :state, :board, :created, :updated, :deadline, :player_x, :player_o)
       """
        )
            .bind("id", game.id)
            .bind("state", game.state)
            .bind("board", game.board.toString())
            .bind("created", game.created.epochSecond)
            .bind("updated", game.updated.epochSecond)
            .bind("deadline", game.deadline?.epochSecond)
            .bind("player_x", game.playerX.id)
            .bind("player_o", game.playerO.id)
            .execute()
    }

    override fun getById(id: UUID): Game? =
        handle.createQuery(
            """
           select games.id, games.state, games.board, games.created, games.updated, games.deadline,
             users_x.id as playerX_id, users_x.username as playerX_username, users_x.password_validation as playerX_password_validation,
             users_o.id as playerO_id, users_o.username as playerO_username, users_o.password_validation as playerO_password_validation
           from dbo.Games games  
           inner join dbo.Users users_x on games.player_x = users_x.id
           inner join dbo.Users users_o on games.player_o = users_o.id
           where games.id = :id
        """
        )
            .bind("id", id)
            .mapTo<GameDbModel>()
            .singleOrNull()
            ?.run {
                toGame()
            }

    override fun update(game: Game) {
        handle.createUpdate(
            """
            update dbo.Games
            set state=:state, board=:board, updated=:updated, deadline=:deadline
            where id=:id
        """
        )
            .bind("id", game.id)
            .bind("state", game.state)
            .bind("board", game.board.toString())
            .bind("updated", game.updated.epochSecond)
            .bind("deadline", game.deadline?.epochSecond)
            .execute()
    }
}

class GameDbModel(
    val id: UUID,
    val state: Game.State,
    val board: Board,
    val created: Instant,
    val updated: Instant,
    val deadline: Instant?,
    @Nested("playerX")
    val playerX: User,
    @Nested("playerO")
    val playerO: User,
) {
    fun toGame() = Game(id, state, board, created, updated, deadline, playerX, playerO)
}