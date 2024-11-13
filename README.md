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

# Найденный баг

[OtelAutoConfiguration](https://github.com/spring-projects-experimental/spring-cloud-sleuth-otel/blob/main/spring-cloud-sleuth-otel-autoconfigure/src/main/java/org/springframework/cloud/sleuth/autoconfig/otel/OtelAutoConfiguration.java)

```java
@Bean 
@ConditionalOnClass(name = "io.opentelemetry.api.metrics.GlobalMeterProvider") 
@ConditionalOnMissingBean 
SpanProcessorProvider otelBatchSpanProcessorProvider(OtelProcessorProperties otelProcessorProperties) { 
    return new SpanProcessorProvider() { 
       @Override 
       public SpanProcessor toSpanProcessor(SpanExporter spanExporter) { 
          BatchSpanProcessorBuilder builder = BatchSpanProcessor.builder(spanExporter); 
          setBuilderProperties(otelProcessorProperties, builder); 
          return builder.build(); 
       } 
   
       private void setBuilderProperties(OtelProcessorProperties otelProcessorProperties, 
             BatchSpanProcessorBuilder builder) { 
          if (otelProcessorProperties.getBatch().getExporterTimeout() != null) { 
             builder.setExporterTimeout(otelProcessorProperties.getBatch().getExporterTimeout(), 
                   TimeUnit.MILLISECONDS); 
          } 
          if (otelProcessorProperties.getBatch().getMaxExportBatchSize() != null) { 
             builder.setMaxExportBatchSize(otelProcessorProperties.getBatch().getMaxExportBatchSize()); 
          } 
          if (otelProcessorProperties.getBatch().getMaxQueueSize() != null) { 
             builder.setMaxQueueSize(otelProcessorProperties.getBatch().getMaxQueueSize()); 
          } 
          if (otelProcessorProperties.getBatch().getScheduleDelay() != null) { 
             builder.setScheduleDelay(otelProcessorProperties.getBatch().getScheduleDelay(), 
                   TimeUnit.MILLISECONDS); 
          } 
       } 
    }; 
} 
   
@Bean 
@ConditionalOnMissingClass("io.opentelemetry.api.metrics.GlobalMeterProvider") 
@ConditionalOnMissingBean 
SpanProcessorProvider otelSimpleSpanProcessorProvider() { 
    return SimpleSpanProcessor::create; 
}
```

Оба бина имеют зависимость на то, чтобы на classpath лежал GlobalMeterProvider, если перейти в него и посмотреть его javadoc, то можно будет увидеть, что в описании к классу сказано:

_**This class is a temporary solution until metrics SDK is marked stable.**_

С версии [v1.10.0-rc.2](https://github.com/open-telemetry/opentelemetry-java/releases/tag/v1.10.0-rc.2) класс GlobalMeterProvider удален.

В javadoc к SimpleSpanProcessor указано, что настоятельно рекомендуется использовать BatchSpanProcessor:

```java
* Returns a new {@link SimpleSpanProcessor} which exports spans to the {@link SpanExporter}
* synchronously.
*
* <p>This processor will cause all spans to be exported directly as they finish, meaning each  
* export request will have a single span. Most backends will not perform well with a single span
* per request so unless you know what you're doing, strongly consider using {@link
* BatchSpanProcessor} instead, including in special environments such as serverless runtimes.
* {@link SimpleSpanProcessor} is generally meant to for logging exporters only.
```

Если положить на classpath пустой класс io.opentelemetry.api.metrics.GlobalMeterProvider, то будет создан BatchSpanProcessor.

Узкое место в SimpleSpanProcessor - это okhttp3 клиент, через который по grpc отливаются спаны

Внутри него есть очередь, через которую общаются потоки приложения и фоновый тредпул okhttp3, работающий с TCP сокетами - https://github.com/square/okhttp/blob/parent-4.11.0/okhttp/src/main/kotlin/okhttp3/Dispatcher.kt#L102

Это обычная ArrayDeque, поэтому все обращения к ней идут в synchronized секциях. И эти секции становятся узким местом в достаточно нагруженном приложении. Начинают тормозить все места, где есть вызовы endSpan. Т.е. практически везде








