package pt.isel.daw.tictactow.http

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import pt.isel.daw.tictactow.http.model.HomeOutputModel
import pt.isel.daw.tictactow.infra.siren

@RestController
class HomeController {

    @GetMapping(Uris.HOME)
    fun getHome() = siren(
        HomeOutputModel(
            credits = "Made for teaching purposes by P. Félix",
        )
    ) {
        link(Uris.home(), Rels.SELF)
        link(Uris.home(), Rels.HOME)
    }
}