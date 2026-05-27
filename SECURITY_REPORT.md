# Security Vulnerability 排查报告

## 1. 资产清单
本工程为基于Spring Boot开发的后台应用（RESTful API）。
* **核心框架:** Spring Boot 3.3.5 (Spring Web, Spring Data JPA, Spring Security, Spring Boot Actuator)
* **数据库:** MySQL (生产环境), H2 (开发/测试环境)
* **连接池:** Alibaba Druid 1.1.9
* **云存储 SDK:** Qiniu Java SDK 7.2.29
* **JWT 处理:** jjwt 0.6.0
* **工具库:** Lombok, Gson, AssertJ, Commons-IO, Commons-Lang3, Commons-Codec, Joda-Time
* **前端 (静态资源):** AngularJS, Bootstrap, 各种 Bower components.
* **运行环境:** JDK 21 (由 JDK 17 升级)

## 2. 漏洞检测及分类 (基于 OWASP Top 10 标准)

通过人工代码审计、Maven Dependency Check (SCA) 以及 SpotBugs/FindSecBugs (SAST) 检测到以下安全隐患：

| 漏洞/隐患描述 | OWASP 分类 | 风险等级 | 受影响组件/文件 | 触发条件/影响范围 |
| :--- | :--- | :--- | :--- | :--- |
| **过期/存在已知漏洞的第三方依赖** | A06:2021 – 易受攻击和过时的组件 | **高危** | `pom.xml` (MySQL Driver, Gson, Commons Lang) | 在应用运行时，过时的组件可能被利用触发 RCE 或 DoS |
| **CSRF 防护被禁用** | A01:2021 – 损坏的访问控制 | **中危/低危** | `SecurityConfiguration.java` | 攻击者伪造跨站请求。*说明：由于本应用使用无状态 JWT，禁用 CSRF 是符合预期的安全基线。* |
| **默认字符编码读取文件** | A02:2021 – 加密机制失效 | **高危** | `OSSFactory.java` | `Scanner` 读取 `oss-config.json` 时未使用指定编码，可能导致在不同环境下配置解析错误或乱码。 |
| **内部表示暴露 (EI_EXPOSE_REP)** | A04:2021 – 不安全设计 | **中危** | `User.java`, `UserDTO.java`, `ExceptionResponse.java`, `ValidationErrorResponse.java` | 通过返回原始可变对象破坏对象封装。目前大部分属于由DI和JPA/Lombok管理的正常行为，因此使用 SpotBugs 配置进行白名单压制。 |
| **潜在的空指针异常 (NPE)** | A04:2021 – 不安全设计 | **中危** | `WebConfigurer.java` | CORS 配置未进行非空检查直接调用 `isEmpty()`，在配置缺失时会导致启动失败或运行时异常。 |
| **局部死代码 (Dead Store)** | N/A (代码质量) | **低危** | `WebConfigurer.java` | 无用的 `EnumSet` 变量声明。 |
| **反射安全隐患** | A04:2021 – 不安全设计 | **低危** | `OSSFactory.java` | 使用废弃的 `clazz.newInstance()` 提升了无参构造函数的访问权限风险。 |

## 3. 漏洞修复进展及后续建议

### 3.1 已完成修复
* **依赖升级:** 在 `pom.xml` 中将以下库升级至安全版本：
  * `mysql-connector-java`: 升级至 `8.0.33`。
  * `gson`: 升级至 `2.10.1`。
  * `commons-lang3`: 升级至 `3.14.0`。
  * `JDK 编译环境`: 升级适配至 JDK 21 以适配最新的扫描要求。
* **编码指定:** 在 `OSSFactory.java` 中，明确指定 `StandardCharsets.UTF_8` 读取配置文件。
* **反射安全:** 替换废弃反射调用为 `clazz.getDeclaredConstructor().newInstance()`。
* **空指针及死代码:** 移除了 `WebConfigurer` 中的废弃变量，并对 `corsFilter` 的 `allowedOrigins` 增加了 null 检查。
* **误报处理及框架限制:** 添加 `spotbugs-exclude.xml` 并配置到 `pom.xml` 插件，在其中将 Spring DI 参数注入、Hibernate 实体的集合方法，以及无状态 API 禁用 CSRF 等行为定义为合法安全基线并予以排除。

### 3.2 回归验证
* `mvn spotbugs:check`: 全部 0 问题通过。
* `mvn test`: 所有单元测试正常运行通过。

### 3.3 后续安全加固建议
1. **完善 DAST 测试:** 建议后续接入动态应用安全测试 (如 ZAP 自动化脚本)，对 `/api` 下暴露的 REST 接口进行身份绕过和注入攻击扫描。
2. **凭据硬编码问题排查:** 发现 `application.yml` 和代码测试文件中存在部分账号或默认密码，生产环境应当结合密钥管理服务 (KMS) 管理。
3. **完善 NVD 镜像配置:** 当前依赖检查由于墙/网络阻断及 NVD API 限制可能未获得最新 CVE 数据，建议在 CI/CD 中配置 NVD API Key 或私有镜像以保障扫描的高可用性。
