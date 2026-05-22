package org.example.cvgenerator.domain.port.`in`

import org.example.cvgenerator.domain.model.Cv
import org.example.cvgenerator.domain.model.Template
import org.example.cvgenerator.domain.model.ExportFormat

interface ExportCvUseCase {
    fun export(cv: Cv, template: Template, format: ExportFormat): ByteArray
}