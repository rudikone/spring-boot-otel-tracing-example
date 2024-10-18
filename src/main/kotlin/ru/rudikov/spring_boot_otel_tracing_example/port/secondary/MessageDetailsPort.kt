package ru.rudikov.spring_boot_otel_tracing_example.port.secondary

interface MessageDetailsPort {

    fun save(clientId: String, sourceMessage: String, encryptString: String?, resultMessage: String, )
}