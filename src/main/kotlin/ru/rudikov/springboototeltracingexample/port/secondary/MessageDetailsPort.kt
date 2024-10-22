package ru.rudikov.springboototeltracingexample.port.secondary

interface MessageDetailsPort {

    fun save(clientId: String, sourceMessage: String, encryptString: String?, resultMessage: String, )
}
