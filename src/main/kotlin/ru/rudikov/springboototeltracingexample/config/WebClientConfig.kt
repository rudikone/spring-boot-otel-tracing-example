package ru.rudikov.springboototeltracingexample.config

import io.micrometer.observation.ObservationRegistry
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.client.WebClient


@Configuration
class WebClientConfig {

    @Bean
    fun webClientBuilder(): WebClient.Builder = WebClient.builder()

    @Bean
    fun webClient(webClientBuilder: WebClient.Builder, observationRegistry: ObservationRegistry): WebClient =
        webClientBuilder.baseUrl("http://localhost:1080").observationRegistry(observationRegistry).build()
}
