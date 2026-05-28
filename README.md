# AuthApp - Spring Boot Login Application

[![English](https://img.shields.io/badge/Language-English-blue.svg)](README.md)
[![简体中文](https://img.shields.io/badge/Language-%E7%AE%80%E4%BD%93%E4%B8%AD%E6%96%87-red.svg)](README-ZhCn.md)

## Project Introduction

AuthApp is a comprehensive sample application demonstrating a secure REST API and frontend integration. It provides standard authentication features including login, logout, user registration with email verification, "remember me" functionality, password reset, and multi-language support. The application is secured using JWT (JSON Web Tokens) and Spring Security.

## Technology Stack

The project utilizes a modern technology stack separated into frontend, backend, infrastructure, and toolchain components.

### 1. Backend (Language & Runtime)
* **Java 21 (Eclipse Temurin JDK 21 LTS):** The core programming language and runtime environment.
* **Spring Boot (3.3.5):** The foundational framework for rapid application development.
* **Spring Security:** Provides authentication and authorization capabilities.
* **Spring Data JPA:** Simplifies data access layer implementation.
* **Spring Boot Actuator:** Offers production-ready features like monitoring and metrics (`/actuator/health`, `/actuator/metrics`).
* **Undertow:** The default embedded web server replacing Tomcat.
* **jjwt (0.6.0):** Used for JSON Web Token generation and validation for stateless authentication.
* **springdoc-openapi (2.0.4):** Generates OpenAPI documentation and provides Swagger UI (`/swagger-ui.html`).
* **Alibaba Druid (1.1.9):** High-performance database connection pool with built-in monitoring (`/druid/index.html`).
* **Lombok (1.18.34):** Reduces boilerplate code (getters, setters, etc.) during development.
* **Joda-Time (2.9.9):** Used for advanced date and time manipulation.
* **JavaMailSender:** Handles sending emails for account verification and password resets.

### 2. Frontend Framework
* **AngularJS (1.5.8):** The core frontend JavaScript framework for building the single-page application (SPA).
* **Bootstrap (3.3.7):** The CSS framework for responsive design and UI components.
* **jQuery (1.9.1 - 3.x):** Required dependency for Bootstrap JavaScript plugins.
* **Angular UI Router:** Manages routing and states within the AngularJS application.
* **angular-translate:** Provides internationalization (i18n) and localization support.
* **Thymeleaf:** Used minimally for serving the initial view or server-side templates if required.

### 3. Infrastructure & Database Systems
* **H2 Database (In-Memory):** The default database for local development and testing. The console is accessible at `/h2-console`.
* **MySQL Connector/J (8.0.33):** The database driver for connecting to a production MySQL database.
* **Qiniu SDK (7.2.x):** Integrated for cloud storage or CDN capabilities.
* **Docker:** Used for containerization and deployment.

### 4. Build Tools & Toolchain
* **Maven (3.8+):** The project management and comprehension tool used for building the application.
* **spotify/dockerfile-maven-plugin (1.3.6):** Automates the building of Docker images during the Maven build lifecycle.
* **SpotBugs Maven Plugin (4.8.2.0):** Static code analysis tool to find potential bugs in Java code, integrated with FindSecBugs for security analysis.

## Environment Requirements

To avoid environment conflicts, ensure the following minimum requirements are met:
* **JDK:** Version 21 (Eclipse Temurin JDK 21 LTS is highly recommended).
* **Maven:** Version 3.8.1 or higher (Note: Maven Wrapper `mvnw` is not included, so Maven must be installed explicitly on your system).
* **Docker:** Required only if you intend to build and run the application as a container.

## Local Deployment & Startup Steps

These steps are compatible with Windows, macOS, and Linux environments.

### 1. Clone the repository
```bash
git clone <repository_url>
cd <repository_directory>
```

### 2. Build the project
Build the application using Maven. You can skip tests during the build if needed.
```bash
mvn clean package -DskipTests
```

### 3. Run the application
Run the application using the Spring Boot Maven plugin. The default application port is `7081`.

**Local Debugging (uses in-memory H2 database):**
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```
*(Alternatively, you can run the built jar: `java -jar target/springboot-login-application-1.0.0.jar`)*

**Production Mode:**
For production, you need to configure the connection string for the MySQL database in the configuration files first.
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

### 4. Docker Deployment (Optional)
Build the Docker image:
```bash
mvn clean package dockerfile:build -DskipTests
```
Run the Docker container:
```bash
docker run -p 7081:7081 -e "SPRING_PROFILES_ACTIVE=local" -m='2g' --name springboot-login-app -d megadotnet/springboot-login-application
```

## Project Structure

```text
├── src
│   ├── main
│   │   ├── java/com/app/login     # Backend Java source code (Controllers, Services, Repositories, Security, etc.)
│   │   └── resources              # Configuration files (application.yml, i18n properties, mails templates)
│   │       └── static             # Frontend static resources (AngularJS app, HTML, CSS, JS, Bower components)
│   └── test                       # Unit and integration tests
├── pom.xml                        # Maven project object model, managing dependencies and plugins
└── Dockerfile                     # Docker configuration for containerizing the application
```

## Development Conventions

* **Code Style:** The project uses standard Java coding conventions. Lombok is heavily utilized; ensure your IDE has the Lombok plugin installed and annotation processing enabled.
* **REST API:** All APIs should be designed following RESTful principles and documented using Swagger/OpenAPI.
* **Security:** All endpoints (except public ones like login/register) must be secured. User inputs should be validated using Spring Boot Validation.
* **Testing:** Write unit tests for all business logic. Run `mvn clean test` before any commit to ensure all tests pass.

## Troubleshooting

1. **Port Already in Use:** If the application fails to start because port 7081 is in use, either stop the conflicting process or change the port in `src/main/resources/application.yml` (`server.port: 7081`).
2. **Lombok Compilation Errors:** If you see "cannot find symbol" errors for getters/setters in your IDE, verify that the Lombok plugin is installed and "Enable annotation processing" is checked in your IDE settings.
3. **Database Connection Issues:** When running with the `prod` profile, ensure your MySQL instance is running and the credentials in your configuration file are correct.
