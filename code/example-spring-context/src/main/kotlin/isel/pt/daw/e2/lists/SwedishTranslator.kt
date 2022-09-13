package isel.pt.daw.e2.lists

import org.springframework.stereotype.Component

@Component
class SwedishTranslator : LanguageTranslator{
    override fun translate(englishWord: String) = if (englishWord == "Hello") {
        "Hej"
    }else {
        null
    }

    override val targetLanguage = "sv"
}