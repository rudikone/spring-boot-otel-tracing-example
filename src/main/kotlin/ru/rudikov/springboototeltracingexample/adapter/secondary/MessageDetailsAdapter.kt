package ru.rudikov.springboototeltracingexample.adapter.secondary

import org.slf4j.MDC
import org.springframework.data.jdbc.core.JdbcAggregateTemplate
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import ru.rudikov.springboototeltracingexample.adapter.model.MessageDetails
import ru.rudikov.springboototeltracingexample.port.secondary.MessageDetailsPort
import java.util.*

@Service
class MessageDetailsAdapter(
    private val jdbcTemplate: JdbcAggregateTemplate
) : MessageDetailsPort {

    @Transactional
    override fun save(clientId: String, sourceMessage: String, encryptString: String?, resultMessage: String) {
        val messageDetails = MessageDetails(
            id = UUID.randomUUID().toString(),
            clientId = clientId,
            sourceMsg = sourceMessage,
            encryptStr = encryptString,
            resultMsg = resultMessage,
            traceId = MDC.get("traceId")
        )

        jdbcTemplate.insert(messageDetails)
    }
}
