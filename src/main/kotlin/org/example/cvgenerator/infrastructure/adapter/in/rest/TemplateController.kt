package org.example.cvgenerator.infrastructure.adapter.`in`.rest

import org.example.cvgenerator.infrastructure.adapter.`in`.rest.dto.TemplateResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api")
class TemplateController {

    @GetMapping("/templates")
    fun getTemplates(): List<TemplateResponse> = listOf(
        TemplateResponse(
            id          = "CLASSIC",
            name        = "Clásico",
            description = "CV tradicional de una columna, sobrio y formal."
        ),
        TemplateResponse(
            id          = "MODERN",
            name        = "Moderno",
            description = "Diseño con columna lateral de color, más visual."
        ),
        TemplateResponse(
            id          = "EUROPASS",
            name        = "Europass",
            description = "Formato oficial europeo reconocido en toda la UE."
        )
    )
}