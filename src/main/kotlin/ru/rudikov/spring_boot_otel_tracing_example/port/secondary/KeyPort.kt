package ru.rudikov.spring_boot_otel_tracing_example.port.secondary

interface KeyPort {

    fun getKey(): String?
}