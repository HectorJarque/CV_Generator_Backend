package org.example.cvgenerator.infrastructure.adapter.`in`.rest

import jakarta.validation.Valid
import org.example.cvgenerator.domain.port.`in`.ExportCvUseCase
import org.example.cvgenerator.infrastructure.adapter.`in`.rest.dto.ExportRequest
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/cv")
class ExportController(
    private val exportCvUseCase: ExportCvUseCase
) {

    @PostMapping("/export")
    fun export(@Valid @RequestBody request: ExportRequest): ResponseEntity<ByteArray> {
        val bytes    = exportCvUseCase.export(
            cv       = request.toCv(),
            template = request.toTemplate(),
            format   = request.toFormat()
        )

        val format      = request.toFormat()
        val contentType = when (format) {
            org.example.cvgenerator.domain.model.ExportFormat.PDF -> "application/pdf"
            org.example.cvgenerator.domain.model.ExportFormat.PNG -> "image/png"
            org.example.cvgenerator.domain.model.ExportFormat.SVG -> "image/svg+xml"
        }
        val extension   = format.name.lowercase()

        val headers = HttpHeaders().apply {
            contentType.let { set(HttpHeaders.CONTENT_TYPE, it) }
            set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"cv.$extension\"")
        }

        return ResponseEntity.ok()
            .headers(headers)
            .body(bytes)
    }
}