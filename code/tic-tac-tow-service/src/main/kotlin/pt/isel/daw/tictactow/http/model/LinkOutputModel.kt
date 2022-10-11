package pt.isel.daw.tictactow.http.model

import pt.isel.daw.tictactow.http.LinkRelation
import java.net.URI

data class LinkOutputModel(
    private val targetUri: URI,
    private val relation: LinkRelation
) {
    val href = targetUri.toASCIIString()
    val rel = relation.value
}