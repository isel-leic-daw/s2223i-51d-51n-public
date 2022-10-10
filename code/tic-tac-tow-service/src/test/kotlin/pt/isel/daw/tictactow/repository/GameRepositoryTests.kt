package pt.isel.daw.tictactow.repository

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.fail
import org.junit.jupiter.api.Test
import pt.isel.daw.tictactow.RealClock
import pt.isel.daw.tictactow.domain.Board
import pt.isel.daw.tictactow.domain.GameLogic
import pt.isel.daw.tictactow.domain.PasswordValidationInfo
import pt.isel.daw.tictactow.domain.Position
import pt.isel.daw.tictactow.repository.jdbi.JdbiGamesRepository
import pt.isel.daw.tictactow.repository.jdbi.JdbiUsersRepository
import pt.isel.daw.tictactow.utils.testWithHandleAndRollback
import java.time.Duration

class GameRepositoryTests {

    @Test
    fun `can create and retrieve`(): Unit = testWithHandleAndRollback { handle ->

        // given: repositories and logic
        val userRepo = JdbiUsersRepository(handle)
        val gameRepo = JdbiGamesRepository(handle)
        val gameLogic = GameLogic(RealClock, Duration.ofMinutes(5))

        // and: two existing users
        userRepo.storeUser("alice", PasswordValidationInfo(""))
        userRepo.storeUser("bob", PasswordValidationInfo(""))

        // when: creating and inserting a game
        val alice = userRepo.getUserByUsername("alice") ?: fail("user must exist")
        val bob = userRepo.getUserByUsername("bob") ?: fail("user must exist")
        val game = gameLogic.createNewGame(alice, bob)
        gameRepo.insert(game)

        // and: retrieving the game
        val retrievedGame = gameRepo.getById(game.id)

        // then: the two games are equal
        assertEquals(game, retrievedGame)

        // when: updating the game
        val newGame = game.copy(board = game.board.mutate(Position(1, 1), Board.State.PLAYER_X))

        // and: storing the game
        gameRepo.update(newGame)

        // and: retrieving the game again
        val newRetrievedGame = gameRepo.getById(game.id)

        // then: the two games are equal
        assertEquals(newGame, newRetrievedGame)
    }
}