package pt.isel.daw.tictactow.repository.jdbi.mappers

import org.jdbi.v3.core.mapper.ColumnMapper
import org.jdbi.v3.core.statement.StatementContext
import java.sql.ResultSet
import java.time.Instant

class InstantMapper : ColumnMapper<Instant> {
    override fun map(rs: ResultSet, columnNumber: Int, ctx: StatementContext): Instant {
        return Instant.ofEpochSecond(rs.getLong(columnNumber))
    }
}