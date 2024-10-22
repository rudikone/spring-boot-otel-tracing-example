package ru.rudikov.springboototeltracingexample

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringBootOtelTracingExampleApplication

fun main(args: Array<String>) {
	runApplication<SpringBootOtelTracingExampleApplication>(*args)
}
