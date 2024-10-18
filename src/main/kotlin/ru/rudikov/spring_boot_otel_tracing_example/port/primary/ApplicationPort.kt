package ru.rudikov.spring_boot_otel_tracing_example.port.primary

import ru.rudikov.spring_boot_otel_tracing_example.application.model.Message

interface ApplicationPort {

    fun encryptMessage(message: Message): Message
}