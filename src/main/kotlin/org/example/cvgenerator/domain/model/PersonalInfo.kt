package org.example.cvgenerator.domain.model

data class PersonalInfo(
    val firstName: String = "",
    val lastName: String = "",
    val jobTitle: String = "",
    val email: String = "",
    val phone: String = "",
    val location: String = "",
    val linkedIn: String = "",
    val website: String = "",
    val photoBase64: String = "",
)
