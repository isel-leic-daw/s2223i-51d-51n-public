package pt.isel.daw.tictactow.domain

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test
import pt.isel.daw.tictactow.Clock
import pt.isel.daw.tictactow.RealClock
import java.time.Duration
import java.time.Instant

class GameLogicTests {

    @Test
    fun `simple game`() {
        // given:a game
        var game = gameLogic.createNewGame(alice, bob)

        // when: alice plays
        var result = gameLogic.applyRound(game, Round(Position(1, 1), alice))

        // then: next player is bob
        game = when (result) {
            is RoundResult.OthersTurn -> result.game
            else -> fail("Unexpected round result $result")
        }
        assertEquals(Game.State.NEXT_PLAYER_O, game.state)

        // when: bob plays
        result = gameLogic.applyRound(game, Round(Position(0, 1), bob))

        // then: next player is alice
        game = when (result) {
            is RoundResult.OthersTurn -> result.game
            else -> fail("Unexpected round result $result")
        }
        assertEquals(Game.State.NEXT_PLAYER_X, game.state)

        // when: alice plays
        result = gameLogic.applyRound(game, Round(Position(0, 0), alice))

        // then: next player is bob
        game = when (result) {
            is RoundResult.OthersTurn -> result.game
            else -> fail("Unexpected round result $result")
        }
        assertEquals(Game.State.NEXT_PLAYER_O, game.state)

        // when: bob plays
        result = gameLogic.applyRound(game, Round(Position(2, 1), bob))

        // then: next player is alice
        game = when (result) {
            is RoundResult.OthersTurn -> result.game
            else -> fail("Unexpected round result $result")
        }
        assertEquals(Game.State.NEXT_PLAYER_X, game.state)

        // when: alice plays
        result = gameLogic.applyRound(game, Round(Position(2, 2), alice))

        // then: alice wins
        game = when (result) {
            is RoundResult.YouWon -> result.game
            else -> fail("Unexpected round result $result")
        }
        assertEquals(Game.State.PLAYER_X_WON, game.state)
    }

    @Test
    fun `cannot play twice`() {
        // given: a game
        var game = gameLogic.createNewGame(alice, bob)

        // when: alice plays
        var result = gameLogic.applyRound(game, Round(Position(1, 1), alice))

        // then: next player is bob
        game = when (result) {
            is RoundResult.OthersTurn -> result.game
            else -> fail("Unexpected round result $result")
        }
        assertEquals(Game.State.NEXT_PLAYER_O, game.state)

        // when: bob plays
        result = gameLogic.applyRound(game, Round(Position(2, 1), bob))

        // then: next player is alice
        game = when (result) {
            is RoundResult.OthersTurn -> result.game
            else -> fail("Unexpected round result $result")
        }
        assertEquals(Game.State.NEXT_PLAYER_X, game.state)

        // when: bob plays
        result = gameLogic.applyRound(game, Round(Position(2, 1), bob))

        // then: result is a failure and next player is still alice
        when (result) {
            is RoundResult.NotYourTurn -> {}
            else -> fail("Unexpected round result $result")
        }
        assertEquals(Game.State.NEXT_PLAYER_X, game.state)
    }

    @Test
    fun `alice wins`() {
        // given: a game and a list of rounds
        val game = gameLogic.createNewGame(alice, bob)
        val rounds = listOf(
            Round(Position(1, 1), alice),
            Round(Position(0, 1), bob),

            Round(Position(0, 0), alice),
            Round(Position(2, 2), bob),

            Round(Position(2, 0), alice),
            Round(Position(0, 2), bob),

            Round(Position(1, 0), alice),
        )

        // when: the rounds are applied
        val result = play(gameLogic, game, rounds)

        // then: alice wins
        when (result) {
            is RoundResult.YouWon -> {
                assertEquals(Game.State.PLAYER_X_WON, result.game.state)
            }
            else -> fail("Unexpected round result $result")
        }
    }

    @Test
    fun `test draw game`() {
        // given: a game and a list of rounds
        val game = gameLogic.createNewGame(alice, bob)
        val rounds = listOf(
            Round(Position(0, 0), alice),
            Round(Position(1, 1), bob),

            Round(Position(2, 0), alice),
            Round(Position(1, 0), bob),

            Round(Position(1, 2), alice),
            Round(Position(0, 1), bob),

            Round(Position(2, 1), alice),
            Round(Position(2, 2), bob),

            Round(Position(0, 2), alice),
        )

        // when: the rounds are applied
        val result = play(gameLogic, game, rounds)

        // then: it's a draw
        when (result) {
            is RoundResult.Draw -> {
                assertEquals(Game.State.DRAW, result.game.state)
            }
            else -> fail("Unexpected round result $result")
        }
    }

    @Test
    fun `timeout test`() {
        // given: a game logic, a game and a list of rounds
        val testClock = TestClock()
        val timeout = Duration.ofMinutes(5)
        val gameLogic = GameLogic(testClock, timeout)
        var game = gameLogic.createNewGame(alice, bob)

        // when: alice plays
        testClock.advance(timeout.minusMinutes(1))
        var result = gameLogic.applyRound(game, Round(Position(1, 1), alice))

        // then: round is accepted
        game = when (result) {
            is RoundResult.OthersTurn -> result.game
            else -> fail("Unexpected result $result")
        }

        // when: bob plays
        testClock.advance(timeout.plusSeconds(1))
        result = gameLogic.applyRound(game, Round(Position(1, 1), bob))

        // then: round is not accepted and alice won
        game = when (result) {
            is RoundResult.TooLate -> result.game
            else -> fail("Unexpected result $result")
        }
        assertEquals(Game.State.PLAYER_X_WON, game.state)
    }

    private fun play(logic: GameLogic, initialGame: Game, rounds: List<Round>): RoundResult? {
        var previousResult: RoundResult? = null
        for (round in rounds) {
            val game = when (previousResult) {
                null -> initialGame
                is RoundResult.OthersTurn -> previousResult.game
                else -> fail("Unexpected round result $previousResult")
            }
            previousResult = logic.applyRound(game, round)
        }
        return previousResult
    }

    companion object {
        private val gameLogic = GameLogic(
            RealClock,
            Duration.ofMinutes(5)
        )

        // our test players
        private val alice = User(1, "alice", PasswordValidationInfo(""))
        private val bob = User(2, "alice", PasswordValidationInfo(""))
    }

    class TestClock : Clock {

        private var now = Instant.ofEpochSecond(0)

        override fun now(): Instant = now

        fun advance(duration: Duration) {
            now += duration
        }
    }
}