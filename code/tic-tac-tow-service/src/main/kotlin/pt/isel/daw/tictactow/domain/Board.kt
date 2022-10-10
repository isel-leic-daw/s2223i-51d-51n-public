package pt.isel.daw.tictactow.domain

data class Board(
    val cells: Array<Array<State>>
) {
    init {
        require(
            cells.size == 3 && cells.all { it.size == 3 }
        )
    }

    fun get(position: Position): State = cells[position.row][position.col]
    fun canPlayOn(position: Position) = get(position) == State.EMPTY
    fun mutate(position: Position, state: State) = Board(
        cells.clone().also {
            cells[position.row][position.col] = state
        }
    )

    fun hasWon(state: State): Boolean {
        require(state != State.EMPTY)
        // TODO can be optimized
        return cells.any { row -> row.all { it == state } } ||
            (0..2).any { col -> cells.all { row -> row[col] == state } } ||
            cells[0][0] == state && cells[1][1] == state && cells[2][2] == state ||
            cells[0][2] == state && cells[1][1] == state && cells[2][0] == state
    }

    companion object {

        fun create() = Board(
            Array(3) {
                Array(3) { State.EMPTY }
            }
        )

        fun fromString(s: String): Board {
            require(s.length == 9)
            return Board(
                arrayOf(
                    arrayOf(
                        State.fromChar(s[0]),
                        State.fromChar(s[1]),
                        State.fromChar(s[2]),
                    ),
                    arrayOf(
                        State.fromChar(s[3]),
                        State.fromChar(s[4]),
                        State.fromChar(s[5]),
                    ),
                    arrayOf(
                        State.fromChar(s[6]),
                        State.fromChar(s[7]),
                        State.fromChar(s[8]),
                    )
                )
            )
        }
    }

    override fun toString(): String = cells.flatMap { row ->
        row.map { it.char }
    }.joinToString("")

    enum class State(val char: Char) {
        EMPTY(' '),
        PLAYER_X('X'),
        PLAYER_O('O'),
        ;

        companion object {
            fun fromChar(c: Char) = when (c) {
                ' ' -> EMPTY
                'X' -> PLAYER_X
                'O' -> PLAYER_O
                else -> throw IllegalArgumentException("Invalid value for Board.State")
            }
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Board

        if (!cells.contentDeepEquals(other.cells)) return false

        return true
    }

    fun isFull(): Boolean = cells.all { row -> row.all { it != State.EMPTY } }

    override fun hashCode(): Int {
        return cells.contentDeepHashCode()
    }
}