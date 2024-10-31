package com.app.login.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * OpenAPIConfig
 * Created by Administrator on 2024/10/31.
 * migrating-from-springfox https://springdoc.org/#migrating-from-springfox
 */
@Configuration
@Profile("!prod")
public class OpenAPIConfig {
    /**
     * 配置OpenAPI信息
     * 该方法定义了应用程序的OpenAPI文档的基本信息，如标题、描述、版本和许可证等
     * 这有助于生成更丰富、更详细的API文档
     *
     * @return OpenAPI 实例，包含API文档的基本信息
     */
    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Login service API")
                        .description("Spring Login sample application")
                        .version("v0.0.1")
                        .license(new License().name("Apache 2.0").url("https://github.com/megadotnet")))
                .externalDocs(new ExternalDocumentation()
                        .description("Spring Login sample application")
                        .url("https://github.com/megadotnet"));
    }

}
