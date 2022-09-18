package isel.pt.daw.e2.lists

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.AnnotationConfigApplicationContext
import org.springframework.stereotype.Component
import org.springframework.beans.factory.getBean

private val log = LoggerFactory.getLogger("main")

interface LanguageTranslator {
    fun translate(englishWord: String): String?
    val targetLanguage: String
}

@Component
class Translator(
    languageTranslators: List<LanguageTranslator>
){
    private val map: Map<String, LanguageTranslator>

    init {
        map = languageTranslators.associateBy {
            it.targetLanguage
        }
    }

    fun translate(englishWord: String, targetLanguage: String) =
        map[targetLanguage]?.translate(englishWord)
            ?: "Sorry, no available translation for '$englishWord' in '$targetLanguage'"

}

fun main() {
    log.info("started")
    // Create the context
    val context = AnnotationConfigApplicationContext()
    // Scan a package for all @Component annotated classes
    context.scan("isel.pt.daw.e2.lists")
    // Refresh the context to take into consideration the new bean definitions
    context.refresh()
    // Get a bean
    val translator = context.getBean<Translator>()

    println(translator.translate("Hello", "pt"))
    println(translator.translate("Hello", "es"))
    println(translator.translate("Hello", "sv"))

    /*
     * Conclusions:
     * - Adding a new translator only requires adding a class implementing the `LanguageTranslator` interface
     *   and annotated with `@Component`.
     * - The context will automatically create an instance of Translator using all discovered `LanguageTranslator`
     *   implementations.
     */
}