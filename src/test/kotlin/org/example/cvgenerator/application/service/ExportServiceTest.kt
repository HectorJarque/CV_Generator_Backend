package org.example.cvgenerator.application.service

import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.example.cvgenerator.domain.model.*
import org.example.cvgenerator.domain.port.out.CvRendererPort
import org.junit.jupiter.api.Assertions.assertArrayEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ExportServiceTest {

    private val pdfRenderer = mockk<CvRendererPort>()
    private val pngRenderer = mockk<CvRendererPort>()
    private val svgRenderer = mockk<CvRendererPort>()

    private val renderers: Map<ExportFormat, CvRendererPort> = mapOf(
        ExportFormat.PDF to pdfRenderer,
        ExportFormat.PNG to pngRenderer,
        ExportFormat.SVG to svgRenderer
    )

    private val service = ExportService(renderers)

    private val sampleCv = Cv(
        personalInfo = PersonalInfo(firstName = "Ana", lastName = "García", jobTitle = "Dev")
    )

    @Test
    fun `export PDF delegates to pdfRenderer`() {
        val expected = byteArrayOf(1, 2, 3)
        every { pdfRenderer.render(sampleCv, Template.CLASSIC) } returns expected

        val result = service.export(sampleCv, Template.CLASSIC, ExportFormat.PDF)

        assertArrayEquals(expected, result)
        verify(exactly = 1) { pdfRenderer.render(sampleCv, Template.CLASSIC) }
    }

    @Test
    fun `export PNG delegates to pngRenderer`() {
        val expected = byteArrayOf(4, 5, 6)
        every { pngRenderer.render(sampleCv, Template.MODERN) } returns expected

        val result = service.export(sampleCv, Template.MODERN, ExportFormat.PNG)

        assertArrayEquals(expected, result)
        verify(exactly = 1) { pngRenderer.render(sampleCv, Template.MODERN) }
    }

    @Test
    fun `export SVG delegates to svgRenderer`() {
        val expected = "<svg/>".toByteArray()
        every { svgRenderer.render(sampleCv, Template.EUROPASS) } returns expected

        val result = service.export(sampleCv, Template.EUROPASS, ExportFormat.SVG)

        assertArrayEquals(expected, result)
        verify(exactly = 1) { svgRenderer.render(sampleCv, Template.EUROPASS) }
    }

    @Test
    fun `export throws when no renderer registered for format`() {
        val emptyRenderers = emptyMap<ExportFormat, CvRendererPort>()
        val serviceWithNoRenderers = ExportService(emptyRenderers)

        assertThrows<UnsupportedOperationException> {
            serviceWithNoRenderers.export(sampleCv, Template.CLASSIC, ExportFormat.PDF)
        }
    }

    @Test
    fun `export does not call wrong renderer`() {
        val expected = byteArrayOf(7, 8, 9)
        every { pdfRenderer.render(any(), any()) } returns expected

        service.export(sampleCv, Template.CLASSIC, ExportFormat.PDF)

        verify(exactly = 0) { pngRenderer.render(any(), any()) }
        verify(exactly = 0) { svgRenderer.render(any(), any()) }
    }
}