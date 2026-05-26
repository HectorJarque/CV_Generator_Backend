package org.example.cvgenerator.infrastructure.adapter.`in`.rest

import io.mockk.*
import org.example.cvgenerator.domain.port.`in`.ExportCvUseCase
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.test.context.TestPropertySource
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.header
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(ExportController::class)
@TestPropertySource(
    properties = [
        "cv.cors.allowed-origins=http://localhost:4200",
        "cv.rate-limit.max-requests=100",
        "cv.rate-limit.window-seconds=60"
    ]
)
class ExportControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var exportCvUseCase: ExportCvUseCase

    // Creamos el Mock de MockK manualmente usando una configuración de test estándar
    @TestConfiguration
    class MockConfig {
        @Bean
        fun exportCvUseCase(): ExportCvUseCase = mockk()
    }

    private val validRequestJson = """
        {
          "cvData": {
            "personalInfo": {
              "firstName": "Ana",
              "lastName": "García",
              "jobTitle": "Desarrolladora",
              "email": "ana@example.com",
              "phone": "600000000",
              "location": "Madrid",
              "linkedIn": "",
              "website": "",
              "photoBase64": ""
            },
            "workExperience": [],
            "education": [],
            "skills": [],
            "languages": [],
            "summary": ""
          },
          "template": "CLASSIC",
          "format": "PDF"
        }
    """.trimIndent()

    @Test
    fun `POST export returns 200 with PDF headers`() {
        // Limpiamos el mock antes de usarlo por si acaso
        clearMocks(exportCvUseCase)
        every { exportCvUseCase.export(any(), any(), any()) } returns byteArrayOf(1, 2, 3)

        mockMvc.perform(
            post("/api/cv/export")
                .contentType(MediaType.APPLICATION_JSON)
                .content(validRequestJson)
        )
            .andExpect(status().isOk)
            .andExpect(header().string("Content-Type", "application/pdf"))
            .andExpect(header().string("Content-Disposition", "attachment; filename=\"cv.pdf\""))
    }

    @Test
    fun `POST export returns 200 with PNG headers`() {
        clearMocks(exportCvUseCase)
        every { exportCvUseCase.export(any(), any(), any()) } returns byteArrayOf(4, 5, 6)

        val pngRequest = validRequestJson.replace("\"format\": \"PDF\"", "\"format\": \"PNG\"")

        mockMvc.perform(
            post("/api/cv/export")
                .contentType(MediaType.APPLICATION_JSON)
                .content(pngRequest)
        )
            .andExpect(status().isOk)
            .andExpect(header().string("Content-Type", "image/png"))
            .andExpect(header().string("Content-Disposition", "attachment; filename=\"cv.png\""))
    }

    @Test
    fun `POST export returns 400 when template is blank`() {
        val invalid = validRequestJson.replace("\"template\": \"CLASSIC\"", "\"template\": \"\"")

        mockMvc.perform(
            post("/api/cv/export")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalid)
        )
            .andExpect(status().isBadRequest)
    }

    @Test
    fun `POST export returns 400 when format is blank`() {
        val invalid = validRequestJson.replace("\"format\": \"PDF\"", "\"format\": \"\"")

        mockMvc.perform(
            post("/api/cv/export")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalid)
        )
            .andExpect(status().isBadRequest)
    }
}