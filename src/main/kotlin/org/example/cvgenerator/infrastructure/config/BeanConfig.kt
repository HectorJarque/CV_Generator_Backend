package org.example.cvgenerator.infrastructure.config

import org.example.cvgenerator.domain.model.ExportFormat
import org.example.cvgenerator.domain.port.out.CvRendererPort
import org.example.cvgenerator.infrastructure.adapter.out.pdf.PdfRenderer
import org.example.cvgenerator.infrastructure.adapter.out.png.PngRenderer
import org.example.cvgenerator.infrastructure.adapter.out.svg.SvgRenderer
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BeanConfig {

    @Bean
    fun renderers(
        pdf: PdfRenderer,
        png: PngRenderer,
        svg: SvgRenderer
    ): Map<ExportFormat, CvRendererPort> = mapOf(
        ExportFormat.PDF to pdf as CvRendererPort,
        ExportFormat.PNG to png as CvRendererPort,
        ExportFormat.SVG to svg as CvRendererPort
    )
}