plugins {
	kotlin("jvm") version "1.9.23"
	kotlin("plugin.spring") version "1.9.23"
	id("org.springframework.boot") version "3.3.4"
	id("io.spring.dependency-management") version "1.1.6"
	id("io.gitlab.arturbosch.detekt") version "1.23.6"
}

group = "ru.rudikov"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

repositories {
	mavenCentral()
}

detekt {
	toolVersion = "1.23.6"
	config.setFrom(file("config/detekt/detekt.yml"))
	buildUponDefaultConfig = true
}

dependencies {
	implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")
	implementation("org.jetbrains.kotlin:kotlin-reflect")

	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.kafka:spring-kafka")
	implementation("org.springframework.boot:spring-boot-starter-logging")
	implementation("org.springframework.boot:spring-boot-starter-webflux") // web client
	implementation("org.springframework.boot:spring-boot-starter-data-jdbc")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

	implementation("io.github.springwolf:springwolf-kafka:1.7.0")
	runtimeOnly("io.github.springwolf:springwolf-ui:1.7.0")

	runtimeOnly("org.postgresql:postgresql")

	implementation("io.micrometer:micrometer-tracing-bridge-otel")
	implementation("io.opentelemetry:opentelemetry-exporter-otlp")
	implementation("net.ttddyy.observation:datasource-micrometer-spring-boot:1.0.5") // для трассировки SQL запросов
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}
