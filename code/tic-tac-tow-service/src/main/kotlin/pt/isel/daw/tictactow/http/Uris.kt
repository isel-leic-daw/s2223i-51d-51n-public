package pt.isel.daw.tictactow.http

import org.springframework.web.util.UriTemplate
import pt.isel.daw.tictactow.domain.Game
import java.net.URI

object Uris {

    const val HOME = "/"
    const val GAME_BY_ID = "/games/{gid}"

    fun home(): URI = URI(HOME)
    fun gameById(game: Game) = UriTemplate(GAME_BY_ID).expand(game.id)

    object Users {
        const val CREATE = "/users"
        const val TOKEN = "/users/token"
        const val GET_BY_ID = "/users/{id}"
        const val HOME = "/me"

        fun byId(id: String) = UriTemplate(GET_BY_ID).expand(id)
        fun home(): URI = URI(HOME)
    }
}