package ru.rudikov.springboototeltracingexample.application.service

import org.springframework.stereotype.Service
import ru.rudikov.springboototeltracingexample.application.model.Message
import ru.rudikov.springboototeltracingexample.port.primary.ApplicationPort
import ru.rudikov.springboototeltracingexample.port.secondary.KeyPort
import ru.rudikov.springboototeltracingexample.port.secondary.MessageDetailsPort

@Service
class MessageEncryptor(
    private val keyPort: KeyPort,
    private val messageDetailsPort: MessageDetailsPort,
) : ApplicationPort {

    override fun encryptMessage(message: Message): Message {
        val sourceString = message.text
        val encryptString = keyPort.getKey()
        val combinedString = encryptString + sourceString
        val encryptedMessage = combinedString.toList().shuffled().joinToString("")

        messageDetailsPort.save(
            clientId = message.clientId,
            sourceMessage = sourceString,
            encryptString = encryptString,
            resultMessage = encryptedMessage,
        )

        return Message(
            clientId = message.clientId,
            text = encryptedMessage,
        )
    }
}
