group = "it.gionatale.fp.orderservice"
version = "1.0-SNAPSHOT"

plugins {
    id("java")
    id("org.springframework.boot") version "3.3.4"
}

apply(plugin = "io.spring.dependency-management")

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.javamoney:moneta:1.1")
    implementation("org.postgresql:postgresql:42.7.4")
    implementation(project(":Commons"))
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    //testImplementation("com.h2database:h2:2.3.232")
}


tasks.test {
    useJUnitPlatform()
}