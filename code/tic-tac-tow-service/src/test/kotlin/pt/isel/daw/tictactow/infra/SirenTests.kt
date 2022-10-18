package pt.isel.daw.tictactow.infra

import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Test
import org.skyscreamer.jsonassert.JSONAssert
import java.net.URI

class SirenTests {

    @Test
    fun `can produce siren representation`() {
        // given: model classes
        class GameModel(
            val name: String,
            val description: String
        )

        class PlayerModel(
            val name: String,
        )

        // and: link relations
        val self = LinkRelation("self")
        val player = LinkRelation("https://example.com/rels/player")

        // and: a Jackson mapper
        val mapper = ObjectMapper()

        // when: producing a Siren model
        val sirenModel = siren(
            GameModel(
                name = "the name",
                description = "the description",
            )
        ) {
            link(URI("https://example.com/games/1"), self)
            entity(PlayerModel("Alice"), player) {
                link(URI("https://example.com/users/1"), self)
            }
            entity(PlayerModel("Bob"), player) {
                link(URI("https://example.com/users/2"), self)
            }
        }

        // and: serializing it to JSON
        val jsonString = mapper.writeValueAsString(sirenModel)

        // then: the serialization is the expected one
        val expected = """
            {
                "properties": {
                    "name": "the name",
                    "description": "the description"
                },
                "entities":[
                    {
                        "rel": ["https://example.com/rels/player"],
                        "properties": {
                            "name": "Alice"
                        },
                        "links": [
                            {"rel": ["self"], "href": "https://example.com/users/1"}
                        ]
                    },
                    {
                        "rel": ["https://example.com/rels/player"],
                        "properties": {
                            "name": "Bob"
                        },
                        "links": [
                            {"rel": ["self"], "href": "https://example.com/users/2"}
                        ]
                    }
                ],
                "links": [
                    {"rel": ["self"], "href": "https://example.com/games/1"}
                ]
            }
        """.trimIndent()
        JSONAssert.assertEquals(expected, jsonString, true)
    }
}