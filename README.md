# CV_Generator

Backend del generador de CV — API REST construida con **Kotlin + Spring Boot**, arquitectura hexagonal y exportación a PDF / PNG / SVG.

El backend es **completamente stateless**: recibe el JSON del CV en cada petición, genera el archivo y lo devuelve. No hay base de datos, no hay sesiones, no hay estado que mantener entre llamadas.

---

## Stack

| Capa | Tecnología |
|---|---|
| Lenguaje | Kotlin 1.9 |
| Framework | Spring Boot 3.x |
| Build | Maven (pom.xml) |
| Exportación PDF | OpenHtmlToPdf + Flying Saucer |
| Exportación SVG/PNG | Batik (Apache) |
| Tests | JUnit 5 + MockK |
| Documentación | SpringDoc OpenAPI (Swagger UI) |
| Despliegue | Koyeb (free tier, 24/7 sin interrupciones) |

---

## Arquitectura: stateless por diseño

El servidor **no guarda nada**. Cada exportación es un ciclo completo y autocontenido:

**Ventajas de este enfoque:**
- Sin base de datos → cero coste, cero mantenimiento
- Sin estado → cualquier instancia puede responder cualquier petición
- Sin sesiones → sin tokens, sin expiración, sin GDPR complicado
- Reinicio del servidor → sin pérdida de datos (no había ninguno)

---

## Configuración local

### Requisitos

- JDK 17+
- IntelliJ IDEA o IDE de elección

### Arrancar el proyecto

```bash
git clone https://github.com/HectorJarque/CV_Generator_Backend.git
cd Backend
./mvnw spring-boot:run
```

La API estará disponible en `http://localhost:8080`.

---

## Tests

```bash
./mvnw test
```

---

## Principios de arquitectura

El proyecto sigue **arquitectura hexagonal** (Ports & Adapters):

- El **dominio** no importa nada de Spring, ni de PDF, ni de HTTP.
- El **caso de uso** en `application/` orquesta el dominio.
- Los **adaptadores** en `infrastructure/` son los únicos que conocen tecnologías externas.
- La **dirección de dependencias** siempre apunta hacia dentro (dominio).

---
