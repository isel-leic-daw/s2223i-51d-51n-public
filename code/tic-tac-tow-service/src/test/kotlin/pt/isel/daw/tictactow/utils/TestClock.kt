package pt.isel.daw.tictactow.utils

import pt.isel.daw.tictactow.Clock
import java.time.Duration
import java.time.Instant

class TestClock : Clock {

    private var now = Instant.ofEpochSecond(0)

    override fun now(): Instant = now

    fun advance(duration: Duration) {
        now += duration
    }
}