package pt.isel.daw.tictactow.http

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import pt.isel.daw.tictactow.http.model.HomeOutputModel
import pt.isel.daw.tictactow.http.model.LinkOutputModel

@RestController
class HomeController {

    @GetMapping(Uris.HOME)
    fun getHome() = HomeOutputModel(
        links = listOf(
            LinkOutputModel(
                Uris.home(),
                LinkRelation.SELF
            ),
            LinkOutputModel(
                Uris.home(),
                LinkRelation.HOME
            ),
        )
    )
}