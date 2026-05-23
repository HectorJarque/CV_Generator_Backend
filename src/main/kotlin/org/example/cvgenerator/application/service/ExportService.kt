package org.example.cvgenerator.application.service

import org.example.cvgenerator.domain.model.Cv
import org.example.cvgenerator.domain.model.ExportFormat
import org.example.cvgenerator.domain.model.Template
import org.example.cvgenerator.domain.port.`in`.ExportCvUseCase
import org.example.cvgenerator.domain.port.out.CvRendererPort
import org.springframework.stereotype.Service

@Service
class ExportService(
    private val renderers: Map<ExportFormat, CvRendererPort>
) : ExportCvUseCase {

    override fun export(cv: Cv, template: Template, format: ExportFormat): ByteArray =
        renderers[format]?.render(cv, template) ?: throw UnsupportedOperationException("No hay renderer registrado para el formato: $format")
}