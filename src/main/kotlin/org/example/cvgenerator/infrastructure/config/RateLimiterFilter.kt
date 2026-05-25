package org.example.cvgenerator.infrastructure.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.atomic.AtomicInteger

@Component
class RateLimitFilter(
    @Value("\${cv.rate-limit.max-requests}") private val maxRequests: Int,
    @Value("\${cv.rate-limit.window-seconds}") private val windowSeconds: Long
) : OncePerRequestFilter() {

    private val requestCounts = ConcurrentHashMap<String, Pair<AtomicInteger, Long>>()

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain
    ) {
        if (! request.requestURI.contains("/api/cv/export")) {
            chain.doFilter(request, response)
            return
        }

        val ip = request.getHeader("X-Forwarded-For")?.split(",")?.first()?.trim()
            ?: request.remoteAddr
        val now = System.currentTimeMillis()
        val windowMs = windowSeconds * 1000

        val entry = requestCounts.compute(ip) { _, existing ->
            when {
                existing == null -> Pair(AtomicInteger(1), now)
                now - existing.second > windowMs -> Pair(AtomicInteger(1), now)
                else -> Pair(existing.first.apply { incrementAndGet() }, existing.second)
            }
        } !!

        if (entry.first.get() > maxRequests) {
            response.status = 429
            response.contentType = "application/json"
            response.writer.write("""{"error":"Too many requests. Inténtalo en $windowSeconds segundos."}""")
            return
        }

        chain.doFilter(request, response)
    }
}