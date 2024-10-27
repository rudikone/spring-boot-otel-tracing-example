# Spring Boot OpenTelemetry example

**Проект использует плагины:**

- kotlin("jvm") version "1.9.23"
- kotlin("plugin.spring") version "1.9.23"
- id("org.springframework.boot") version "2.7.18"
- id("io.spring.dependency-management") version "1.0.15.RELEASE"

**Проект использует зависимости:**

- spring-boot-starter-actuator version 2.7.18
- spring-kafka version 2.8.11
- spring-boot-starter-webflux version 2.7.18
- spring-boot-starter-data-jdbc version 2.7.18
- spring-cloud-starter-sleuth version 3.1.11
- spring-cloud-sleuth-otel-autoconfigure version 1.1.4
- io.opentelemetry:opentelemetry-exporter-otlp version 1.24.0
- p6spy:p6spy version 3.9.1

**Описание:**
Демонстрационный проект, читает сообщение с текстом пользователя из топика [request-topic](src/main/kotlin/ru/rudikov/springboototeltracingexample/adapter/primary/Consumer.kt),
запрашивает ключ шифрования через http запрос к [http://localhost:1080/key](src/main/kotlin/ru/rudikov/springboototeltracingexample/adapter/secondary/KeyAdapter.kt),
шифрует текст пользователя, [сохраняет](src/main/kotlin/ru/rudikov/springboototeltracingexample/adapter/secondary/MessageDetailsAdapter.kt) артефакты в БД и отправляет ответ в [response-topic](src/main/kotlin/ru/rudikov/springboototeltracingexample/adapter/primary/Producer.kt).

**Use case:**
1. Для запуска необходимой инфраструктуры выполни docker-compose up см. [docker-compose](docker-compose.yml)
2. Запустить проект
3. Отправить [MyRequest](src/main/kotlin/ru/rudikov/springboototeltracingexample/adapter/model/MyRequest.kt), добавив **_b3_** header (4bf92f3577b34da6a3ce929d0e0e4736-00f067aa0ba902b7-1) в сообщение
   ```shell
   curl -X POST "http://localhost:8082/v3/clusters/1/topics/request-topic/records" \
     -H "Content-Type: application/json" \
     -H "Accept: application/json" \
     -d '{
        "value": {
           "type": "JSON",
           "data": {
                 "clientId": "example-client",
                 "fakeId": "fake-123",
                 "message": "Hello, Kafka!"
           }
        },
        "headers": [
          {
            "name": "b3",
            "value": "NGJmOTJmMzU3N2IzNGRhNmEzY2U5MjlkMGUwZTQ3MzYtMDBmMDY3YWEwYmE5MDJiNy0x"
          }
        ]
     }'
   ```
   или добавив **_traceparent_** header (00-0af7651916cd43dd8448eb211c80319c-b7ad6b7169203331-01)
   ```shell
   curl -X POST "http://localhost:8082/v3/clusters/1/topics/request-topic/records" \
     -H "Content-Type: application/json" \
     -H "Accept: application/json" \
     -d '{
        "value": {
           "type": "JSON",
           "data": {
                 "clientId": "example-client",
                 "fakeId": "fake-456",
                 "message": "Hello, Kafka!"
           }
        },
        "headers": [
          {
            "name": "traceparent",
            "value": "MDAtMGFmNzY1MTkxNmNkNDNkZDg0NDhlYjIxMWM4MDMxOWMtYjdhZDZiNzE2OTIwMzMzMS0wMQ=="
          }
        ]
     }'
   ```
   **_ВНИМАНИЕ_**: 
      - value header необходимо закодировать с помощью [base64](https://www.base64encode.org/) - ограничение confluent kafka-rest
      - не поддерживается propagation type w3c и b3 одновременно
4. Перейти на [jaeger ui](http://localhost:16686/), проверить трейсы








