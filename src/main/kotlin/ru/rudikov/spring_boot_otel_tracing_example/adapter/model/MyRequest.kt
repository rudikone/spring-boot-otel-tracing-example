package ru.rudikov.spring_boot_otel_tracing_example.adapter.model

data class MyRequest(
    val clientId: String,
    val fakeId: String,
    val message: String,
)
