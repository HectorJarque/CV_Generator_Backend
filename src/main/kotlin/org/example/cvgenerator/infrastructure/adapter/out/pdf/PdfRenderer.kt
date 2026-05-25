package org.example.cvgenerator.infrastructure.adapter.out.pdf

import com.itextpdf.html2pdf.HtmlConverter
import org.example.cvgenerator.domain.model.Cv
import org.example.cvgenerator.domain.model.Template
import org.example.cvgenerator.domain.port.out.CvRendererPort
import org.example.cvgenerator.infrastructure.adapter.out.template.TemplateLoader
import org.springframework.stereotype.Component
import java.io.ByteArrayOutputStream

@Component
class PdfRenderer(
    private val templateLoader: TemplateLoader
) : CvRendererPort {

    override fun render(cv: Cv, template: Template): ByteArray {
        val fileName     = "${template.name.lowercase()}.html"  // "classic.html", "modern.html"...
        val xhtmlContent = templateLoader.load(fileName).replacePlaceholders(cv)

        val baos = ByteArrayOutputStream()
        HtmlConverter.convertToPdf(xhtmlContent, baos)
        return baos.toByteArray()
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
            val period  = if (exp.isCurrent) "${exp.startDate} — Actualidad" else "${exp.startDate} — ${exp.endDate}"
            val content = if (exp.bulletPoints.isNotEmpty())
                "<ul>${exp.bulletPoints.joinToString("") { "<li>$it</li>" }}</ul>"
            else "<p>${exp.description}</p>"
            """
            <div class="work-entry">
              <div class="entry-header">
                <span class="entry-title">${exp.position}</span>
                <span class="entry-period">$period</span>
              </div>
              <span class="entry-subtitle">${exp.company} · ${exp.location}</span>
              $content
            </div>
            """.trimIndent()
        })

        result = result.replace("{{education}}", cv.education.joinToString("\n") { edu ->
            val period = if (edu.isCurrent) "${edu.startDate} — Actualidad" else "${edu.startDate} — ${edu.endDate}"
            """
            <div class="education-entry">
              <div class="entry-header">
                <span class="entry-title">${edu.degree} — ${edu.fieldOfStudy}</span>
                <span class="entry-period">$period</span>
              </div>
              <span class="entry-subtitle">${edu.institution} · ${edu.location}</span>
              ${if (edu.description.isNotBlank()) "<p>${edu.description}</p>" else ""}
            </div>
            """.trimIndent()
        })

        result = result.replace("{{skills}}", cv.skills.joinToString("\n") { skill ->
            """<div class="skill-entry"><span>${skill.name}</span><span class="skill-level">${skill.level.name}</span></div>"""
        })

        result = result.replace("{{languages}}", cv.languages.joinToString("\n") { lang ->
            """<div class="language-entry"><span>${lang.name}</span><span class="language-level">${lang.level.name}</span></div>"""
        })

        return result
    }
}