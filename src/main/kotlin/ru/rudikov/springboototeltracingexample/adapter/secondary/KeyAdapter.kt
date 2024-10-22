package ru.rudikov.springboototeltracingexample.adapter.secondary

import org.springframework.core.ParameterizedTypeReference
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import ru.rudikov.springboototeltracingexample.adapter.model.KeyResponse
import ru.rudikov.springboototeltracingexample.port.secondary.KeyPort

@Service
class KeyAdapter(
    private val webClient: WebClient,
) : KeyPort {

    override fun getKey(): String? = webClient
        .method(HttpMethod.GET)
        .uri("/key")
        .retrieve()
        .toEntity(object : ParameterizedTypeReference<KeyResponse>() {})
        .block()
        ?.body
        ?.key
}
