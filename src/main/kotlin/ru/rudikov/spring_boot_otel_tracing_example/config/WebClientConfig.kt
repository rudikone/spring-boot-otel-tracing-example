package ru.rudikov.spring_boot_otel_tracing_example.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient


@Configuration
class WebClientConfig {

    @Bean
    fun webClientBuilder(): WebClient.Builder = WebClient.builder()

    @Bean
    fun webClient(webClientBuilder: WebClient.Builder): WebClient =
        webClientBuilder.baseUrl("http://localhost:1080").build()
}