import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.7.4"
	id("io.spring.dependency-management") version "1.0.14.RELEASE"
	kotlin("jvm") version "1.6.21"
	kotlin("plugin.spring") version "1.6.21"
	java
}

group = "pt.isel.daw"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
	mavenCentral()
}

val ktlint by configurations.creating

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

	implementation("org.springframework.security:spring-security-core:5.7.3")

	// for JDBI
	implementation("org.jdbi:jdbi3-core:3.32.0")
	implementation("org.jdbi:jdbi3-kotlin:3.32.0")
	implementation("org.postgresql:postgresql:42.5.0")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation(kotlin("test"))

	ktlint("com.pinterest:ktlint:0.47.1")
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs = listOf("-Xjsr305=strict")
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

task<Exec>("dbTestsUp") {
    commandLine("docker-compose", "up", "-d", "--build", "--force-recreate", "db-tests")
}

task<Exec>("dbTestsWait") {
    commandLine("docker", "exec", "db-tests", "/app/bin/wait-for-postgres.sh", "localhost")
    dependsOn("dbTestsUp")
}

task<Exec>("dbTestsDown") {
    commandLine("docker-compose", "down")
}

// from https://pinterest.github.io/ktlint/install/integrations/#custom-gradle-integration-with-kotlin-dsl
val outputDir = "${project.buildDir}/reports/ktlint/"
val inputFiles = project.fileTree(mapOf("dir" to "src", "include" to "**/*.kt"))

val ktlintCheck by tasks.creating(JavaExec::class) {
	inputs.files(inputFiles)
	outputs.dir(outputDir)

	description = "Check Kotlin code style."
	classpath = ktlint
	mainClass.set("com.pinterest.ktlint.Main")
	// see https://pinterest.github.io/ktlint/install/cli/#command-line-usage for more information
	args = listOf("src/**/*.kt")
}

tasks.named("check") {
	dependsOn(ktlintCheck)
    dependsOn("dbTestsWait")
    finalizedBy("dbTestsDown")
}