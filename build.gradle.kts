plugins {
	kotlin("jvm") version "1.9.23"
	kotlin("plugin.spring") version "1.9.23"
	id("org.springframework.boot") version "2.7.18"
	id("io.spring.dependency-management") version "1.0.15.RELEASE"
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

	implementation("org.springframework.kafka:spring-kafka")
	implementation("org.springframework.boot:spring-boot-starter-logging")
	// web client
	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("org.springframework.boot:spring-boot-starter-data-jdbc")

	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

	runtimeOnly("org.postgresql:postgresql")

	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.cloud:spring-cloud-starter-sleuth:3.1.11") {
		// Это заменяет стандартную реализацию Brave, на реализацию, основанную на OpenTelemetry.
		exclude("org.springframework.cloud", "spring-cloud-sleuth-brave")
	}
	implementation("org.springframework.cloud:spring-cloud-sleuth-otel-autoconfigure:1.1.4")
	// с более новыми версиями не работает
	implementation("io.opentelemetry:opentelemetry-exporter-otlp:1.24.0")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}
