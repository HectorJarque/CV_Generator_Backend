package org.example.cvgenerator.domain.model

data class WorkExperience(
    val company: String = "",
    val position: String = "",
    val location: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val isCurrent: Boolean = false,
    val description: String = "",
    val bulletPoints: List<String> = emptyList(),
)
