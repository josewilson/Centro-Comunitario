plugins {
	java
	id("org.springframework.boot") version "3.3.3"
	id("io.spring.dependency-management") version "1.1.6"
}

group = "com.centrocomunitario"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(17)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {

	// Swagger 2 (Springfox)
	implementation ("io.springfox:springfox-swagger2:3.0.0")

	// OpenAPI 3 (Springdoc)
	implementation ("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.3.0")

	// Implementação do MongoDB e Web
	implementation("org.springframework.boot:spring-boot-starter-data-mongodb")
	implementation("org.springframework.boot:spring-boot-starter-web")

	// Lombok para reduzir código repetitivo
	compileOnly("org.projectlombok:lombok")
	annotationProcessor("org.projectlombok:lombok")

	// Teste
	testImplementation("org.springframework.boot:spring-boot-starter-test")

	// JUnit 5 para testes
	testImplementation("org.junit.jupiter:junit-jupiter-api")
	testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

	// Mockito para mocks nos testes unitários
	testImplementation("org.mockito:mockito-core:4.0.0")
	testImplementation("org.mockito:mockito-junit-jupiter:4.0.0")

	// Plataforma JUnit para o Spring Boot Test
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")

}

tasks.withType<Test> {
	useJUnitPlatform()
}
