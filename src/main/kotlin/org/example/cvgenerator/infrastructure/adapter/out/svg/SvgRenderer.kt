package org.example.cvgenerator.infrastructure.adapter.out.svg

import org.example.cvgenerator.domain.model.Cv
import org.example.cvgenerator.domain.model.Template
import org.example.cvgenerator.domain.port.out.CvRendererPort
import org.example.cvgenerator.infrastructure.adapter.out.template.HtmlSanitizer.sanitize
import org.example.cvgenerator.infrastructure.adapter.out.template.TemplateLoader
import org.springframework.stereotype.Component

@Component
class SvgRenderer(
    private val templateLoader: TemplateLoader
) : CvRendererPort {

    override fun render(cv: Cv, template: Template): ByteArray {
        val fileName = "${template.name.lowercase()}.svg"
        val svgContent = templateLoader.load(fileName).replacePlaceholders(cv)
        return svgContent.toByteArray(Charsets.UTF_8)
    }

    private fun String.replacePlaceholders(cv: Cv): String {
        var result = this

        with(cv.personalInfo) {
            result = result
                .replace("{{firstName}}", firstName.sanitize())
                .replace("{{lastName}}", lastName.sanitize())
                .replace("{{fullName}}", "$firstName $lastName".trim().sanitize())
                .replace("{{jobTitle}}", jobTitle.sanitize())
                .replace("{{email}}", email.sanitize())
                .replace("{{phone}}", phone.sanitize())
                .replace("{{location}}", location.sanitize())
                .replace("{{linkedIn}}", linkedIn.sanitize())
                .replace("{{website}}", website.sanitize())
        }

        result = result.replace("{{summary}}", cv.summary.sanitize())

        result = result.replace("{{workExperience}}", cv.workExperience.joinToString("\n") { exp ->
            buildWorkExperienceBlock(
                company = exp.company.sanitize(),
                position = exp.position.sanitize(),
                startDate = exp.startDate.sanitize(),
                endDate = exp.endDate.sanitize(),
                isCurrent = exp.isCurrent,
                description = exp.description.sanitize()
            )
        })

        result = result.replace("{{education}}", cv.education.joinToString("\n") { edu ->
            buildEducationBlock(
                institution = edu.institution.sanitize(),
                degree = edu.degree.sanitize(),
                fieldOfStudy = edu.fieldOfStudy.sanitize(),
                startDate = edu.startDate.sanitize(),
                endDate = edu.endDate.sanitize()
            )
        })

        result = result.replace("{{skills}}", cv.skills.joinToString("\n") { skill ->
            buildSkillBlock(name = skill.name.sanitize(), level = skill.level.name)
        })

        result = result.replace("{{languages}}", cv.languages.joinToString("\n") { lang ->
            buildLanguageBlock(name = lang.name.sanitize(), level = lang.level.name)
        })

        return result
    }

    private fun buildWorkExperienceBlock(
        company: String, position: String,
        startDate: String, endDate: String,
        isCurrent: Boolean, description: String
    ): String {
        val period = if (isCurrent) "$startDate — Actualidad" else "$startDate — $endDate"
        return """
            <g class="work-entry">
              <text class="entry-title">$position</text>
              <text class="entry-subtitle">$company · $period</text>
              <text class="entry-description">$description</text>
            </g>
        """.trimIndent()
    }

    private fun buildEducationBlock(
        institution: String, degree: String, fieldOfStudy: String,
        startDate: String, endDate: String
    ): String = """
        <g class="education-entry">
          <text class="entry-title">$degree — $fieldOfStudy</text>
          <text class="entry-subtitle">$institution · $startDate — $endDate</text>
        </g>
    """.trimIndent()

    private fun buildSkillBlock(name: String, level: String): String = """
        <g class="skill-entry">
          <text class="skill-name">$name</text>
          <text class="skill-level">$level</text>
        </g>
    """.trimIndent()

    private fun buildLanguageBlock(name: String, level: String): String = """
        <g class="language-entry">
          <text class="language-name">$name</text>
          <text class="language-level">$level</text>
        </g>
    """.trimIndent()
}