package org.example.cvgenerator.infrastructure.adapter.out.svg

import org.example.cvgenerator.domain.model.Cv
import org.example.cvgenerator.domain.model.Template
import org.example.cvgenerator.domain.port.out.CvRendererPort
import org.springframework.stereotype.Component

@Component
class SvgRenderer : CvRendererPort {

    override fun render(cv: Cv, template: Template): ByteArray {
        TODO("SvgRenderer pendiente de implementar")
    }
}