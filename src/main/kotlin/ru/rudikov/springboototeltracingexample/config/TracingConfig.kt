package ru.rudikov.springboototeltracingexample.config

import io.micrometer.tracing.exporter.FinishedSpan
import io.micrometer.tracing.exporter.SpanExportingPredicate
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class TracingConfig {

    @Bean
    fun noSwagger(): SpanExportingPredicate {
        return SpanExportingPredicate { span: FinishedSpan ->
            span.tags["http.url"] == null || span.tags["http.url"]?.startsWith("/springwolf") == false
        }
    }
}
