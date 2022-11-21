plugins {
    id("org.springframework.boot") version "2.6.6"
    id("io.spring.dependency-management") version "1.0.11.RELEASE"
    id ("java")
}

group = "com.halftusk.authentication"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_11
extra["springCloudVersion"] = "2021.0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation ("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation ("org.springframework.boot:spring-boot-starter-data-redis"){
        exclude("io.lettuce:lettuce-core")
    }
    implementation("redis.clients:jedis:4.2.3")
    implementation ("org.springframework.boot:spring-boot-starter-security")
    implementation ("org.springframework.boot:spring-boot-starter-validation")
    implementation("com.amazonaws:aws-java-sdk-elasticache:1.12.290")
    developmentOnly ("org.springframework.boot:spring-boot-devtools")
    implementation ("io.jsonwebtoken:jjwt:0.9.1")
    implementation("javax.validation:validation-api:2.0.1.Final")
    implementation("org.apache.commons:commons-text:1.9")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation ("com.amazonaws:aws-java-sdk-ses:1.12.290")
    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")
    runtimeOnly ("org.postgresql:postgresql")
    annotationProcessor ("org.springframework.boot:spring-boot-configuration-processor")
    testImplementation ("org.springframework.boot:spring-boot-starter-test")
    testImplementation ("org.springframework.security:spring-security-test")
}

dependencyManagement {
    imports {
        mavenBom("org.springframework.cloud:spring-cloud-dependencies:${property("springCloudVersion")}")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
