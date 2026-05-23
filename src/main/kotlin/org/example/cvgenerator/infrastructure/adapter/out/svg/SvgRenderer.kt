package org.example.cvgenerator.infrastructure.adapter.out.png

import org.example.cvgenerator.domain.model.Cv
import org.example.cvgenerator.domain.model.Template
import org.example.cvgenerator.domain.port.out.CvRendererPort
import org.springframework.stereotype.Component

@Component
class PngRenderer : CvRendererPort {

    override fun render(cv: Cv, template: Template): ByteArray {
        TODO("PngRenderer pendiente de implementar")
    }
}