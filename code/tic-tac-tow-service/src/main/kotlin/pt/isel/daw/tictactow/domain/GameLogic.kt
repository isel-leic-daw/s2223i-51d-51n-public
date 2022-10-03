package pt.isel.daw.tictactow.domain

import pt.isel.daw.tictactow.Clock
import java.time.Duration
import java.time.Instant
import java.util.*

class GameLogic(
    private val clock: Clock,
    private val timeout: Duration,
) {

    fun createNewGame(
        playerX: User,
        playerO: User,
    ): Game {
        val now = clock.now()
        return Game(
            id = UUID.randomUUID(),
            state = Game.State.NEXT_PLAYER_X,
            board = Board.create(),
            created = now,
            updated = now,
            deadline = now + timeout,
            playerX = playerX,
            playerO = playerO,
        )
    }

    fun applyRound(
        game: Game,
        round: Round,
    ): RoundResult {
        if (round.player.id != game.playerO.id && round.player.id != game.playerX.id) {
            return RoundResult.NotAPlayer
        }
        val now = clock.now()
        return when (game.state) {
            Game.State.PLAYER_X_WON -> RoundResult.GameAlreadyEnded
            Game.State.PLAYER_O_WON -> RoundResult.GameAlreadyEnded
            Game.State.DRAW -> RoundResult.GameAlreadyEnded
            Game.State.NEXT_PLAYER_X -> applyRound(game, round, now, PLAYER_X_LOGIC)
            Game.State.NEXT_PLAYER_O -> applyRound(game, round, now, PLAYER_O_LOGIC)
        }
    }

    private fun applyRound(
        game: Game,
        round: Round,
        now: Instant,
        aux: PlayerLogic,
    ): RoundResult = if (!aux.isTurn(game, round.player)) {
        RoundResult.NotYourTurn
    } else {
        if (now > game.deadline) {
            val newGame = game.copy(state = aux.otherWon, deadline = null)
            RoundResult.TooLate(newGame)
        } else {
            if (game.board.canPlayOn(round.position)) {
                val newBoard = game.board.mutate(round.position, aux.boardState)
                if (newBoard.hasWon(aux.boardState)) {
                    val newGame =
                        game.copy(board = newBoard, state = aux.iWon, deadline = null)
                    RoundResult.YouWon(newGame)
                } else {
                    if (newBoard.isFull()) {
                        val newGame = game.copy(
                            board = newBoard,
                            state = Game.State.DRAW,
                            deadline = null,
                        )
                        RoundResult.Draw(newGame)
                    } else {
                        val newGame = game.copy(
                            board = newBoard,
                            state = aux.nextPlayer,
                            deadline = now + timeout,
                        )
                        RoundResult.OthersTurn(newGame)
                    }
                }
            } else {
                RoundResult.PositionNotAvailable
            }
        }
    }

    class PlayerLogic(
        val isTurn: (game: Game, user: User) -> Boolean,
        val otherWon: Game.State,
        val iWon: Game.State,
        val nextPlayer: Game.State,
        val boardState: Board.State,
    )

    companion object {
        private val PLAYER_X_LOGIC = PlayerLogic(
            isTurn = { game, user -> game.isPlayerX(user) },
            otherWon = Game.State.PLAYER_O_WON,
            iWon = Game.State.PLAYER_X_WON,
            nextPlayer = Game.State.NEXT_PLAYER_O,
            boardState = Board.State.PLAYER_X,
        )
        private val PLAYER_O_LOGIC = PlayerLogic(
            isTurn = { game, user -> game.isPlayerO(user) },
            otherWon = Game.State.PLAYER_X_WON,
            iWon = Game.State.PLAYER_O_WON,
            nextPlayer = Game.State.NEXT_PLAYER_X,
            boardState = Board.State.PLAYER_O,
        )
    }
}

sealed class RoundResult {
    object NotYourTurn : RoundResult()
    object GameAlreadyEnded : RoundResult()
    object NotAPlayer : RoundResult()
    object PositionNotAvailable : RoundResult()
    data class TooLate(val game: Game) : RoundResult()
    data class YouWon(val game: Game) : RoundResult()
    data class OthersTurn(val game: Game) : RoundResult()
    data class Draw(val game: Game) : RoundResult()
}

private fun Game.isPlayerX(player: User) = this.playerX.id == player.id

private fun Game.isPlayerO(player: User) = this.playerO.id == player.id