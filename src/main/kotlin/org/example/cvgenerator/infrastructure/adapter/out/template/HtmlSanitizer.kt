package org.example.cvgenerator.infrastructure.adapter.out.template

object HtmlSanitizer {

    fun String.sanitize(): String = this
        .replace("&", "&amp;")
        .replace("<", "&lt;")
        .replace(">", "&gt;")
        .replace("\"", "&quot;")
        .replace("'", "&#39;")
        .trim()
}