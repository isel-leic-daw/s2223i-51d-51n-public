package pt.isel.daw.tictactow.domain

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class BoardTests {

    @Test
    fun `can serialize and deserialize`() {
        val board = Board.create()
        val s = board.toString()
        assertEquals("         ", s)

        val newBoard = Board.fromString(s)
        assertEquals(board, newBoard)
    }

    @Test
    fun `serialization is done by rows`() {
        val board = Board.create()
        val newBoard = board.mutate(Position(0, 1), Board.State.PLAYER_X)
        assertEquals("   X     ", newBoard.toString())
    }
}