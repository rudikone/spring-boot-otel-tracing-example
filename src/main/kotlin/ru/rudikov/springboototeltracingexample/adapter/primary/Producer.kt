package ru.rudikov.springboototeltracingexample.adapter.primary

import mu.KotlinLogging
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import ru.rudikov.springboototeltracingexample.adapter.model.EncryptResult

@Component
class Producer(private val kafkaTemplate: KafkaTemplate<String, EncryptResult>) {

    fun sendMessage(key: String, encryptResult: EncryptResult) {
        kafkaTemplate.sendDefault(key, encryptResult).addCallback(
            /* successCallback = */ { result ->
                val topic = result?.recordMetadata?.topic()

                logger.info {
                    """
                        Сообщение с id=${encryptResult.fakeId} успешно отправлено: topic=${topic}
                    """.trimIndent()
                }
            },
            /* failureCallback = */ { ex ->
                logger.error {
                    "Ошибка при отправке сообщения: ${ex.message}"
                }
            }
        )
    }

    companion object {
        private val logger = KotlinLogging.logger { }
    }
}
