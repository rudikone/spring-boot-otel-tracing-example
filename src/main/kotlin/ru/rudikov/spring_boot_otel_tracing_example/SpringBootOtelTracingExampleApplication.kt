package ru.rudikov.spring_boot_otel_tracing_example

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class SpringBootOtelTracingExampleApplication

fun main(args: Array<String>) {
	runApplication<SpringBootOtelTracingExampleApplication>(*args)
}
