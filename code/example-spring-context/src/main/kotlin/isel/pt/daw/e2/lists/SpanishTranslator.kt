package isel.pt.daw.e2.lists

import org.springframework.stereotype.Component

@Component
class SpanishTranslator : LanguageTranslator {

    override fun translate(englishWord: String): String? = map[englishWord]
    override val targetLanguage: String = "en"

    companion object {
        private val map = mapOf(
            "Hello" to "Hola"
        )
    }
}