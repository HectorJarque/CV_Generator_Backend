package org.example.cvgenerator.infrastructure.adapter.out.png

import org.apache.batik.transcoder.TranscoderInput
import org.apache.batik.transcoder.TranscoderOutput
import org.apache.batik.transcoder.image.PNGTranscoder
import org.example.cvgenerator.domain.model.Cv
import org.example.cvgenerator.domain.model.Template
import org.example.cvgenerator.domain.port.out.CvRendererPort
import org.example.cvgenerator.infrastructure.adapter.out.svg.SvgRenderer
import org.springframework.stereotype.Component
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

@Component
class PngRenderer(
    private val svgRenderer: SvgRenderer
) : CvRendererPort {

    override fun render(cv: Cv, template: Template): ByteArray {
        val svgBytes = svgRenderer.render(cv, template)

        val transcoder = PNGTranscoder()
        val input      = TranscoderInput(ByteArrayInputStream(svgBytes))
        val baos       = ByteArrayOutputStream()
        val output     = TranscoderOutput(baos)

        transcoder.transcode(input, output)
        return baos.toByteArray()
    }
}