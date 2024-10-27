# Spring Boot OpenTelemetry example

[Документация](https://docs.spring.io/spring-boot/reference/actuator/tracing.html) 

**Проект использует плагины:**

- kotlin("jvm") version "1.9.23"
- kotlin("plugin.spring") version "1.9.23"
- id("org.springframework.boot") version "3.3.4"
- id("io.spring.dependency-management") version "1.1.6"

**Проект использует зависимости:**

- spring-boot-starter-actuator version 3.3.4
- spring-kafka version 3.2.4
- spring-boot-starter-webflux version 3.3.4
- spring-boot-starter-data-jdbc version 3.3.4
- io.micrometer:micrometer-tracing-bridge-otel version 1.3.4
- io.opentelemetry:opentelemetry-exporter-otlp version 1.37.0
- net.ttddyy.observation:datasource-micrometer-spring-boot version 1.0.5

**Описание:**
Демонстрационный проект, читает сообщение с текстом пользователя из
топика [request-topic](src/main/kotlin/ru/rudikov/springboototeltracingexample/adapter/primary/Consumer.kt),
запрашивает ключ шифрования через http запрос
к [http://localhost:1080/key](src/main/kotlin/ru/rudikov/springboototeltracingexample/adapter/secondary/KeyAdapter.kt),
шифрует текст
пользователя, [сохраняет](src/main/kotlin/ru/rudikov/springboototeltracingexample/adapter/secondary/MessageDetailsAdapter.kt)
артефакты в БД
и отправляет ответ
в [response-topic](src/main/kotlin/ru/rudikov/springboototeltracingexample/adapter/primary/Producer.kt).

**Use case:**
1. Для запуска необходимой инфраструктуры выполни docker-compose up см. [docker-compose](docker-compose.yml)
2. Запустить проект
3. Перейти на [asyncapi-ui](http://localhost:8080/springwolf/asyncapi-ui.html)
4. Отправить [MyRequest](src/main/kotlin/ru/rudikov/springboototeltracingexample/adapter/model/MyRequest.kt), добавив
   **_traceparent_** header (пример 00-4bf92f3577b34da6a3ce929d0e0e4736-00f067aa0ba902b7-01) или
   **_b3_** header (пример 4bf92f3577b34da6a3ce929d0e0e4736-00f067aa0ba902b7-1) в сообщение
5. Перейти на [jaeger ui](http://localhost:16686/), проверить трейсы

**Примечание**

По умолчанию используется протокол HTTP для коллектора (см. https://github.com/ThomasVitale/spring-boot-opentelemetry?tab=readme-ov-file). Чтобы реализовать выгрузку по gRPC, необходимо 
сконфигурировать:

```java
  @Bean
  OtlpGrpcSpanExporter otlpGrpcSpanExporter() {
  return OtlpGrpcSpanExporter.builder()
  .setEndpoint(otlpProperties.getCollectorUrl())
  .build();
  }
```