package pt.isel.daw.tictactow.repository

interface TransactionManager {
    fun <R> run(block: (Transaction) -> R): R
}