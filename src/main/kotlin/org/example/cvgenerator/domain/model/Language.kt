package org.example.cvgenerator.domain.model

data class Language(
    val name: String = "",
    val level: LanguageLevel = LanguageLevel.B1
)
enum class LanguageLevel {
    A1,
    A2,
    B1,
    B2,
    C1,
    C2,
    NATIVE
}
