package org.example.cvgenerator.infrastructure.adapter.`in`.rest.dto

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import org.example.cvgenerator.domain.model.*

data class ExportRequest(
    @field:Valid
    val cvData: CvDto = CvDto(),
    @field:NotBlank
    val template: String = "CLASSIC",
    @field:NotBlank
    val format: String = "PDF"
) {
    fun toCv(): Cv = cvData.toDomain()
    fun toTemplate(): Template = Template.valueOf(template.uppercase())
    fun toFormat(): ExportFormat = ExportFormat.valueOf(format.uppercase())
}

data class CvDto(
    val personalInfo: PersonalInfoDto = PersonalInfoDto(),
    val workExperience: List<WorkExperienceDto> = emptyList(),
    val education: List<EducationDto> = emptyList(),
    val skills: List<SkillDto> = emptyList(),
    val languages: List<LanguageDto> = emptyList(),
    val summary: String = ""
) {
    fun toDomain() = Cv(
        personalInfo  = personalInfo.toDomain(),
        workExperience = workExperience.map { it.toDomain() },
        education     = education.map { it.toDomain() },
        skills        = skills.map { it.toDomain() },
        languages     = languages.map { it.toDomain() },
        summary       = summary
    )
}

data class PersonalInfoDto(
    val firstName:   String = "",
    val lastName:    String = "",
    val jobTitle:    String = "",
    val email:       String = "",
    val phone:       String = "",
    val location:    String = "",
    val linkedIn:    String = "",
    val website:     String = "",
    val photoBase64: String = ""
) {
    fun toDomain() = PersonalInfo(
        firstName, lastName, jobTitle, email, phone, location, linkedIn, website, photoBase64
    )
}

data class WorkExperienceDto(
    val company:      String = "",
    val position:     String = "",
    val location:     String = "",
    val startDate:    String = "",
    val endDate:      String = "",
    val isCurrent:    Boolean = false,
    val description:  String = "",
    val bulletPoints: List<String> = emptyList()
) {
    fun toDomain() = WorkExperience(
        company, position, location, startDate, endDate, isCurrent, description, bulletPoints
    )
}

data class EducationDto(
    val institution:  String = "",
    val degree:       String = "",
    val fieldOfStudy: String = "",
    val location:     String = "",
    val startDate:    String = "",
    val endDate:      String = "",
    val isCurrent:    Boolean = false,
    val description:  String = ""
) {
    fun toDomain() = Education(
        institution, degree, fieldOfStudy, location, startDate, endDate, isCurrent, description
    )
}

data class SkillDto(
    val name:  String = "",
    val level: String = "INTERMEDIATE"
) {
    fun toDomain() = Skill(
        name  = name,
        level = runCatching { SkillLevel.valueOf(level.uppercase()) }
            .getOrDefault(SkillLevel.INTERMEDIATE)
    )
}

data class LanguageDto(
    val name:  String = "",
    val level: String = "B1"
) {
    fun toDomain() = Language(
        name  = name,
        level = runCatching { LanguageLevel.valueOf(level.uppercase()) }
            .getOrDefault(LanguageLevel.B1)
    )
}