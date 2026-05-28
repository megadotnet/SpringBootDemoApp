# AuthApp - Spring Boot 登录认证应用

[![English](https://img.shields.io/badge/Language-English-blue.svg)](README.md)
[![简体中文](https://img.shields.io/badge/Language-%E7%AE%80%E4%BD%93%E4%B8%AD%E6%96%87-red.svg)](README-ZhCn.md)

## 项目介绍

AuthApp 是一个综合性的示例应用程序，展示了安全的 REST API 和前端集成。它提供标准的身份验证功能，包括登录、注销、带有电子邮件验证的用户注册、“记住我”功能、密码重置以及多语言支持。该应用程序使用 JWT (JSON Web Tokens) 和 Spring Security 进行安全保护。

## 技术栈清单

本项目采用了现代化的技术栈，分为前端、后端、基础设施和工具链四个核心部分。

### 1. 后端 (语言与运行环境)
* **Java 21 (Eclipse Temurin JDK 21 LTS):** 核心编程语言及运行环境。
* **Spring Boot (3.3.5):** 用于快速构建应用程序的基础框架。
* **Spring Security:** 提供身份验证和授权功能。
* **Spring Data JPA:** 简化数据访问层的实现。
* **Spring Boot Actuator:** 提供生产级别的监控和指标统计功能 (`/actuator/health`, `/actuator/metrics`)。
* **Undertow:** 默认的嵌入式 Web 服务器，用于替代 Tomcat。
* **jjwt (0.6.0):** 用于生成和验证 JSON Web Token，实现无状态身份验证。
* **springdoc-openapi (2.0.4):** 自动生成 OpenAPI 文档并提供 Swagger UI 界面 (`/swagger-ui.html`)。
* **Alibaba Druid (1.1.9):** 高性能数据库连接池，内置监控功能 (`/druid/index.html`)。
* **Lombok (1.18.34):** 消除 Java 代码中的样板代码（如 getters, setters 等），提高开发效率。
* **Joda-Time (2.9.9):** 用于高级日期和时间处理。
* **JavaMailSender:** 处理电子邮件发送，用于账户验证和密码重置。

### 2. 前端框架
* **AngularJS (1.5.8):** 核心前端 JavaScript 框架，用于构建单页面应用程序 (SPA)。
* **Bootstrap (3.3.7):** CSS 框架，用于响应式设计和 UI 组件。
* **jQuery (1.9.1 - 3.x):** Bootstrap JavaScript 插件所依赖的基础库。
* **Angular UI Router:** 管理 AngularJS 应用程序中的路由和状态。
* **angular-translate:** 提供国际化 (i18n) 和本地化支持。
* **Thymeleaf:** 主要用于提供初始视图或在需要时进行服务器端模板渲染。

### 3. 基础设施与数据库系统
* **H2 Database (内存数据库):** 默认用于本地开发和测试的数据库。可通过 `/h2-console` 访问控制台。
* **MySQL Connector/J (8.0.33):** 数据库驱动程序，用于连接生产环境的 MySQL 数据库。
* **Qiniu SDK (7.2.x):** 集成七牛云，用于云存储或 CDN 功能。
* **Docker:** 用于应用程序的容器化和部署。

### 4. 工具链与构建依赖
* **Maven (3.8+):** 项目管理和构建工具。
* **spotify/dockerfile-maven-plugin (1.3.6):** 在 Maven 构建生命周期中自动化构建 Docker 镜像。
* **SpotBugs Maven Plugin (4.8.2.0):** 静态代码分析工具，用于发现 Java 代码中潜在的 bug，并集成了 FindSecBugs 进行安全漏洞分析。

## 环境依赖要求

为避免环境冲突，请确保满足以下最低要求：
* **JDK:** 最低版本 21 (强烈建议使用 Eclipse Temurin JDK 21 LTS)。
* **Maven:** 最低版本 3.8.1 (注意：项目中未包含 Maven Wrapper `mvnw`，因此必须在系统环境变量中显式安装配置 Maven)。
* **Docker:** 仅当您计划以容器形式构建和运行应用程序时才需要。

## 本地部署与启动步骤

以下步骤适用于主流的 Windows、macOS 和 Linux 开发环境。

### 1. 克隆代码仓库
```bash
git clone <repository_url>
cd <repository_directory>
```

### 2. 编译打包项目
使用 Maven 构建应用程序。如果不需要在构建时执行测试，可以跳过测试。
```bash
mvn clean package -DskipTests
```

### 3. 运行应用程序
使用 Spring Boot Maven 插件运行应用程序。默认的应用端口配置为 `7081`。

**本地调试模式 (使用 H2 内存数据库):**
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=local
```
*(或者，您可以直接运行构建好的 jar 包: `java -jar target/springboot-login-application-1.0.0.jar`)*

**生产环境模式:**
对于生产环境，需要先在配置文件中配置 MySQL 数据库的连接字符串。
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

### 4. Docker 部署 (可选)
构建 Docker 镜像：
```bash
mvn clean package dockerfile:build -DskipTests
```
运行 Docker 容器：
```bash
docker run -p 7081:7081 -e "SPRING_PROFILES_ACTIVE=local" -m='2g' --name springboot-login-app -d megadotnet/springboot-login-application
```

## 项目结构说明

```text
├── src
│   ├── main
│   │   ├── java/com/app/login     # 后端 Java 源代码 (Controllers, Services, Repositories, Security 等)
│   │   └── resources              # 配置文件 (application.yml, 国际化 properties 文件, 邮件模板)
│   │       └── static             # 前端静态资源 (AngularJS 应用代码, HTML, CSS, JS, Bower 依赖)
│   └── test                       # 单元测试和集成测试代码
├── pom.xml                        # Maven 项目对象模型，管理项目依赖和构建插件
└── Dockerfile                     # Docker 配置文件，用于应用程序容器化
```

## 开发规范

* **代码风格:** 项目遵循标准的 Java 编码规范。项目大量使用了 Lombok；请确保您的 IDE 安装了 Lombok 插件并开启了注解处理 (Annotation Processing)。
* **REST API:** 所有 API 都应遵循 RESTful 原则进行设计，并使用 Swagger/OpenAPI 进行文档化。
* **安全规范:** 所有端点（除了登录/注册等公开接口外）必须受到安全保护。必须使用 Spring Boot Validation 验证所有用户输入。
* **测试规范:** 为所有核心业务逻辑编写单元测试。在提交代码之前，请运行 `mvn clean test` 确保所有测试均能顺利通过。

## 常见问题排查

1. **端口被占用 (Port Already in Use):** 如果应用程序因为 7081 端口被占用而无法启动，请停止冲突进程或在 `src/main/resources/application.yml` 中修改端口配置 (`server.port: 7081`)。
2. **Lombok 编译错误:** 如果您的 IDE 提示找不到 getters/setters 相关的 symbol，请检查 IDE 是否已安装 Lombok 插件，并确认在设置中勾选了 "Enable annotation processing"。
3. **数据库连接失败:** 当使用 `prod` 配置文件启动时，请确保您的 MySQL 实例正在运行，且配置文件中的用户名和密码等连接信息完全正确。
