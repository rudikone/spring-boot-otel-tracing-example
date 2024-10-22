package ru.rudikov.springboototeltracingexample.adapter.model

data class EncryptResult(
    val clientId: String,
    val fakeId: String,
    val status: EncryptResultStatus,
    val text: String?,
)

enum class EncryptResultStatus {
    SUCCESS, ERROR
}
