package org.example.cvgenerator.infrastructure.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.info.License
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {

    @Bean
    fun openApi(): OpenAPI = OpenAPI()
        .info(
            Info()
                .title("CV Generator API")
                .description(
                    "API stateless para generación y exportación de CVs en PDF, PNG y SVG. " +
                            "No almacena ningún dato: recibe el CV en cada petición y devuelve el archivo generado."
                )
                .version("1.0.0")
                .license(License().name("MIT").url("https://opensource.org/licenses/MIT"))
        )
}