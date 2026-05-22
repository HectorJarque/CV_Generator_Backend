package org.example.cvgenerator.domain.port.out

import org.example.cvgenerator.domain.model.Cv
import org.example.cvgenerator.domain.model.Template

interface CvRendererPort {
    fun render(cv: Cv, template: Template): ByteArray
}