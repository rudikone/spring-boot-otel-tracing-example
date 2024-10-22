package ru.rudikov.springboototeltracingexample.adapter.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table("message_details")
data class MessageDetails(
    @Id val id: String,
    val clientId: String,
    val sourceMsg: String,
    val encryptStr: String?,
    val resultMsg: String,
    val traceId: String
)
