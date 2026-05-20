package org.example.cvgenerator.domain.model

data class Skill(
    val name: String = "",
    val level: SkillLevel = SkillLevel.INTERMEDIATE
)

enum class SkillLevel {
    BASIC,
    INTERMEDIATE,
    ADVANCED,
    EXPERT
}