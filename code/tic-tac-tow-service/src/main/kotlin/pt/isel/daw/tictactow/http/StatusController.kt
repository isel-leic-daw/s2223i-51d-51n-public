package pt.isel.daw.tictactow.http

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import pt.isel.daw.tictactow.http.model.StatusOutputModel
import pt.isel.daw.tictactow.infra.siren
import pt.isel.daw.tictactow.repository.TransactionManager
import java.net.InetAddress

@RestController
class StatusController(
    val transactionManager: TransactionManager
) {

    @GetMapping(Uris.STATUS)
    fun getStatus() = transactionManager.run { transaction ->
        siren(
            StatusOutputModel(
                hostname = System.getenv("HOSTNAME"),
                gamesCount = transaction.gamesRepository.count()
            )
        ) {
            // For now, nothing more to add.
        }
    }

    @GetMapping(Uris.STATUS_HOSTNAME)
    fun getStatusHostname(): String = System.getenv("HOSTNAME")

    @GetMapping(Uris.STATUS_IP)
    fun getStatusIp(): String = InetAddress.getLocalHost().hostAddress
}