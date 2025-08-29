package ru.rudikov.springboototeltracingexample.config

import io.opentelemetry.api.baggage.propagation.W3CBaggagePropagator
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator
import io.opentelemetry.context.propagation.TextMapPropagator
import io.opentelemetry.extension.trace.propagation.B3Propagator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Баг в [org.springframework.cloud.sleuth.otel.propagation.CompositeTextMapPropagator]
 * При использовании одновременно b3 и w3c создается baggage,
 * и по условию в 136 строке в контекст проходит пустой трейс.
 * Если не настроить два отдельных TextMapPropagator, то сервис не сможет принимать w3c и b3 одновременно,
 * а только то, что будет указано первым в spring.sleuth.propagation.type
 * */
@Configuration
class TracingConfig {

    @Bean
    fun b3OtelTextMapPropagator(): TextMapPropagator {
        return B3Propagator.injectingMultiHeaders()
    }

    @Bean
    fun w3cOtelTextMapPropagator(): TextMapPropagator {
        return TextMapPropagator.composite(W3CTraceContextPropagator.getInstance(), W3CBaggagePropagator.getInstance())
    }
}