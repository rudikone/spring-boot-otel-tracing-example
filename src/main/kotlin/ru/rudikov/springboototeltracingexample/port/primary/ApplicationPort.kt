package ru.rudikov.springboototeltracingexample.port.primary

import ru.rudikov.springboototeltracingexample.application.model.Message

interface ApplicationPort {

    fun encryptMessage(message: Message): Message
}
