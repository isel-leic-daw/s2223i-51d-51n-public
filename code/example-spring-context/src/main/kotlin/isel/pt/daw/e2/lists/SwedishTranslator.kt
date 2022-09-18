package isel.pt.daw.e2.lists

import org.springframework.stereotype.Component

@Component
class SwedishTranslator : LanguageTranslator {

    override fun translate(englishWord: String) = if(englishWord.lowercase() == "hello") {
        "hej"
    } else {
        null
    }

    override val targetLanguage: String = "sv"

}