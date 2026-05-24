package org.example.cvgenerator.infrastructure.adapter.out.svg

import org.example.cvgenerator.domain.model.Cv
import org.example.cvgenerator.domain.model.Template
import org.example.cvgenerator.domain.port.out.CvRendererPort
import org.example.cvgenerator.infrastructure.adapter.out.template.TemplateLoader
import org.springframework.stereotype.Component

@Component
class SvgRenderer(
    private val templateLoader: TemplateLoader
) : CvRendererPort {

    override fun render(cv: Cv, template: Template): ByteArray {
        val fileName  = "${template.name.lowercase()}.svg"   // "classic.svg", "modern.svg"…
        val svgContent = templateLoader.load(fileName).replacePlaceholders(cv)
        return svgContent.toByteArray(Charsets.UTF_8)
    }

    private fun String.replacePlaceholders(cv: Cv): String {
        var result = this

        with(cv.personalInfo) {
            result = result
                .replace("{{firstName}}",   firstName)
                .replace("{{lastName}}",    lastName)
                .replace("{{fullName}}",    "$firstName $lastName".trim())
                .replace("{{jobTitle}}",    jobTitle)
                .replace("{{email}}",       email)
                .replace("{{phone}}",       phone)
                .replace("{{location}}",    location)
                .replace("{{linkedIn}}",    linkedIn)
                .replace("{{website}}",     website)
        }

        result = result.replace("{{summary}}", cv.summary)

        result = result.replace("{{workExperience}}", cv.workExperience.joinToString("\n") { exp ->
            buildWorkExperienceBlock(exp.company, exp.position, exp.startDate, exp.endDate, exp.isCurrent, exp.description)
        })

        result = result.replace("{{education}}", cv.education.joinToString("\n") { edu ->
            buildEducationBlock(edu.institution, edu.degree, edu.fieldOfStudy, edu.startDate, edu.endDate)
        })

        result = result.replace("{{skills}}", cv.skills.joinToString("\n") { skill ->
            buildSkillBlock(skill.name, skill.level.name)
        })

        result = result.replace("{{languages}}", cv.languages.joinToString("\n") { lang ->
            buildLanguageBlock(lang.name, lang.level.name)
        })

        return result
    }

    private fun buildWorkExperienceBlock(
        company: String, position: String,
        startDate: String, endDate: String, isCurrent: Boolean,
        description: String
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