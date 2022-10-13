package pt.isel.daw.tictactow.http.model

import org.springframework.http.ResponseEntity
import java.net.URI

class Problem(
    typeUri: URI
) {
    val type = typeUri.toASCIIString()

    companion object {
        const val MEDIA_TYPE = "application/problem+json"
        fun response(status: Int, problem: Problem) = ResponseEntity
            .status(status)
            .header("Content-Type", MEDIA_TYPE)
            .body(problem)

        val userAlreadyExists = Problem(
            URI(
                "https://github.com/isel-leic-daw/s2223i-51d-51n-public/tree/main/code/tic-tac-tow-service/" +
                    "docs/problems/user-already-exists"
            )
        )
        val insecurePassword = Problem(
            URI(
                "https://github.com/isel-leic-daw/s2223i-51d-51n-public/tree/main/code/tic-tac-tow-service/" +
                    "docs/problems/insecure-password"
            )
        )

        val userOrPasswordAreInvalid = Problem(
            URI(
                "https://github.com/isel-leic-daw/s2223i-51d-51n-public/tree/main/code/tic-tac-tow-service/" +
                    "docs/problems/user-or-password-are-invalid"
            )
        )
    }
}