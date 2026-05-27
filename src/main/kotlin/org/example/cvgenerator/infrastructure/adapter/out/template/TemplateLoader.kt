package org.example.cvgenerator.infrastructure.adapter.out.template

import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component

@Component
class TemplateLoader {
    fun load(fileName: String): String =
        ClassPathResource("templates/$fileName")
            .inputStream
            .bufferedReader(Charsets.UTF_8)
            .readText()
}