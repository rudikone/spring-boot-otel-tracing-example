package ru.rudikov.spring_boot_otel_tracing_example.adapter.primary

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component
import ru.rudikov.spring_boot_otel_tracing_example.adapter.model.EncryptResult
import mu.KotlinLogging

@Component
class Producer(private val kafkaTemplate: KafkaTemplate<String, EncryptResult>) {

    fun sendMessage(key: String, encryptResult: EncryptResult) {
        kafkaTemplate.sendDefault(key, encryptResult).whenComplete { result, ex ->
            if (ex == null) {
                logger.info {
                    "Сообщение с id=${encryptResult.fakeId} успешно отправлено: topic=${result.recordMetadata.topic()}"
                }
            } else {
                logger.error {
                    "Ошибка при отправке сообщения: ${ex.message}"
                }
            }
        }
    }

    companion object {
        private val logger = KotlinLogging.logger { }
    }
}