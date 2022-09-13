package isel.pt.daw.e2.lists

import org.springframework.stereotype.Component

@Component
class SpanishTranslator : LanguageTranslator {

    override fun translate(englishWord: String): String? = map[englishWord]
    override val targetLanguage: String = "es"

    companion object {
        private val map = mapOf(
            "Hello" to "Hola"
        )
    }
}