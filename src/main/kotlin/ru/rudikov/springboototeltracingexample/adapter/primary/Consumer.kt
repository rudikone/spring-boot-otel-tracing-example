package ru.rudikov.springboototeltracingexample.adapter.primary

import mu.KotlinLogging
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component
import ru.rudikov.springboototeltracingexample.adapter.model.EncryptResult
import ru.rudikov.springboototeltracingexample.adapter.model.EncryptResultStatus.ERROR
import ru.rudikov.springboototeltracingexample.adapter.model.EncryptResultStatus.SUCCESS
import ru.rudikov.springboototeltracingexample.adapter.model.MyRequest
import ru.rudikov.springboototeltracingexample.application.model.Message
import ru.rudikov.springboototeltracingexample.port.primary.ApplicationPort

@Component
class Consumer(
    private val application: ApplicationPort,
    private val producer: Producer,
) {

    @KafkaListener(topics = ["request-topic"])
    fun processMessage(request: MyRequest) {
        runCatching {
            logger.info { "Сообщение с id=${request.fakeId} получено" }

            val message = Message(
                clientId = request.clientId,
                text = request.message,
            )

            application.encryptMessage(message)
        }.onSuccess { resultMessage ->
            val result = EncryptResult(
                clientId = request.clientId,
                fakeId = request.fakeId,
                status = SUCCESS,
                text = resultMessage.text
            )

            producer.sendMessage(key = request.fakeId, encryptResult = result)
        }.onFailure {
            logger.error("Ошибка обработки сообщения с id=${request.fakeId}", it)

            val result = EncryptResult(
                clientId = request.clientId,
                fakeId = request.fakeId,
                status = ERROR,
                text = null
            )

            producer.sendMessage(key = request.fakeId, encryptResult = result)
        }
    }

    companion object {
        private val logger = KotlinLogging.logger { }
    }
}
