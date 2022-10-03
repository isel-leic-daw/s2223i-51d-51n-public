package pt.isel.daw.tictactow

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class TicTacTowApplication

fun main(args: Array<String>) {
	runApplication<TicTacTowApplication>(*args)
}
