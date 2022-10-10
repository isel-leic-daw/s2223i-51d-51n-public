package pt.isel.daw.tictactow.repository.jdbi.mappers

import org.jdbi.v3.core.mapper.ColumnMapper
import org.jdbi.v3.core.statement.StatementContext
import org.postgresql.util.PGobject
import pt.isel.daw.tictactow.domain.Board
import pt.isel.daw.tictactow.repository.jdbi.JdbiGamesRepository
import java.sql.ResultSet
import java.sql.SQLException

class BoardMapper : ColumnMapper<Board> {
    @Throws(SQLException::class)
    override fun map(r: ResultSet, columnNumber: Int, ctx: StatementContext?): Board {
        val obj = r.getObject(columnNumber, PGobject::class.java)
        return JdbiGamesRepository.deserializeBoardFromJson(obj.value ?: throw IllegalArgumentException("TODO"))
    }
}