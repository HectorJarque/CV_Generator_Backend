package org.example.cvgenerator.infrastructure.adapter.out.pdf

import com.itextpdf.html2pdf.HtmlConverter
import org.example.cvgenerator.domain.model.Cv
import org.example.cvgenerator.domain.model.Template
import org.example.cvgenerator.domain.port.out.CvRendererPort
import org.example.cvgenerator.infrastructure.adapter.out.template.HtmlSanitizer.sanitize
import org.example.cvgenerator.infrastructure.adapter.out.template.TemplateLoader
import org.springframework.stereotype.Component
import java.io.ByteArrayOutputStream

@Component
class PdfRenderer(
    private val templateLoader: TemplateLoader
) : CvRendererPort {

    override fun render(cv: Cv, template: Template): ByteArray {
        val fileName = "${template.name.lowercase()}.html"
        val xhtmlContent = templateLoader.load(fileName).replacePlaceholders(cv)

        val baos = ByteArrayOutputStream()
        HtmlConverter.convertToPdf(xhtmlContent, baos)
        return baos.toByteArray()
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
            val period = if (exp.isCurrent) "${exp.startDate.sanitize()} — Actualidad"
            else "${exp.startDate.sanitize()} — ${exp.endDate.sanitize()}"
            val content = if (exp.bulletPoints.isNotEmpty())
                "<ul>${exp.bulletPoints.joinToString("") { "<li>${it.sanitize()}</li>" }}</ul>"
            else "<p>${exp.description.sanitize()}</p>"
            """
            <div class="work-entry">
              <div class="entry-header">
                <span class="entry-title">${exp.position.sanitize()}</span>
                <span class="entry-period">$period</span>
              </div>
              <span class="entry-subtitle">${exp.company.sanitize()} · ${exp.location.sanitize()}</span>
              $content
            </div>
            """.trimIndent()
        })

        result = result.replace("{{education}}", cv.education.joinToString("\n") { edu ->
            val period = if (edu.isCurrent) "${edu.startDate.sanitize()} — Actualidad"
            else "${edu.startDate.sanitize()} — ${edu.endDate.sanitize()}"
            """
            <div class="education-entry">
              <div class="entry-header">
                <span class="entry-title">${edu.degree.sanitize()} — ${edu.fieldOfStudy.sanitize()}</span>
                <span class="entry-period">$period</span>
              </div>
              <span class="entry-subtitle">${edu.institution.sanitize()} · ${edu.location.sanitize()}</span>
              ${if (edu.description.isNotBlank()) "<p>${edu.description.sanitize()}</p>" else ""}
            </div>
            """.trimIndent()
        })

        result = result.replace("{{skills}}", cv.skills.joinToString("\n") { skill ->
            """<div class="skill-entry"><span>${skill.name.sanitize()}</span><span class="skill-level">${skill.level.name}</span></div>"""
        })

        result = result.replace("{{languages}}", cv.languages.joinToString("\n") { lang ->
            """<div class="language-entry"><span>${lang.name.sanitize()}</span><span class="language-level">${lang.level.name}</span></div>"""
        })

        return result
    }
}