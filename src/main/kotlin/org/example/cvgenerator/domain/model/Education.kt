package org.example.cvgenerator.domain.model

data class Education(
    val institution: String = "",
    val degree: String = "",
    val fieldOfStudy: String = "",
    val location: String = "",
    val startDate: String = "",
    val endDate: String = "",
    val isCurrent: Boolean = false,
    val description: String = "",
)
