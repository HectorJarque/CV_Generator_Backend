package org.example.cvgenerator.domain.model

data class Cv(
    val personalInfo: PersonalInfo = PersonalInfo(),
    val workExperience: List<WorkExperience> = emptyList(),
    val education: List<Education> = emptyList(),
    val skills: List<Skill> = emptyList(),
    val languages: List<Language> = emptyList(),
    val summary: String = ""
)
