package pt.isel.daw.tictactow.domain

import java.time.Instant
import java.util.UUID

data class Game(
    val id: UUID,
    val state: State,
    val board: Board,
    val created: Instant,
    val updated: Instant,
    val deadline: Instant?,
    val playerX: User,
    val playerO: User,
) {

    enum class State {
        NEXT_PLAYER_X,
        NEXT_PLAYER_O,
        PLAYER_X_WON,
        PLAYER_O_WON,
        DRAW,
        ;

        val isEnded: Boolean
            get() = this == PLAYER_X_WON || this == PLAYER_O_WON || this == DRAW
    }
}