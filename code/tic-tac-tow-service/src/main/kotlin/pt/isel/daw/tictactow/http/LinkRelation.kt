package pt.isel.daw.tictactow.http

class LinkRelation(
    val value: String
) {
    companion object {

        val SELF = LinkRelation("self")

        val HOME = LinkRelation(
            "https://github.com/isel-leic-daw/s2223i-51d-51n-public/tree/main/code/tic-tac-tow-service/docs/" +
                "rels/home"
        )
    }
}