package pt.isel.daw.tictactow.infra

import java.net.URI

data class SirenModel<T>(
    val properties: T,
    val links: List<LinkModel>,
    val entities: List<EntityModel<*>>,
)

data class LinkModel(
    val rel: List<String>,
    val href: String,
)

data class EntityModel<T>(
    val properties: T,
    val links: List<LinkModel>,
    val rel: List<String>,
)

class SirenBuilderScope<T>(
    val properties: T,
) {
    private val links = mutableListOf<LinkModel>()
    private val entities = mutableListOf<EntityModel<*>>()

    fun link(href: URI, rel: LinkRelation) {
        links.add(LinkModel(listOf(rel.value), href.toASCIIString()))
    }

    fun <U> entity(value: U, rel: LinkRelation, block: EntityBuilderScope<U>.() -> Unit) {
        val scope = EntityBuilderScope(value, listOf(rel.value))
        scope.block()
        entities.add(scope.build())
    }

    fun build(): SirenModel<T> = SirenModel(
        properties = properties,
        links = links,
        entities = entities,
    )
}

class EntityBuilderScope<T>(
    val properties: T,
    val rel: List<String>,
) {
    private val links = mutableListOf<LinkModel>()

    fun link(href: URI, rel: LinkRelation) {
        links.add(LinkModel(listOf(rel.value), href.toASCIIString()))
    }

    fun build(): EntityModel<T> = EntityModel(
        properties = properties,
        links = links,
        rel = rel,
    )
}

fun <T> siren(value: T, block: SirenBuilderScope<T>.() -> Unit): SirenModel<T> {
    val scope = SirenBuilderScope(value)
    scope.block()
    return scope.build()
}