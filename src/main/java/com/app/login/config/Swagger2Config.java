package com.app.login.config;

// 导入必要的包
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Swagger2配置类，用于配置Swagger2文档生成工具的相关设置。
 * 创建者：Administrator
 * 创建时间：2018/6/3 0003
 */
@Configuration // 标记此类为配置类
@EnableSwagger2 // 启用Swagger2注解
@Profile("!prod") // 指定配置文件生效的环境，这里是非生产环境
public class Swagger2Config {

    // 定义一个Bean，用于创建Docket实例，Docket是Swagger提供的一个构建API文档的类
    @Bean
    public Docket createRestApi() {
        // 创建Docket实例，指定文档类型为SWAGGER_2
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo()) // 设置API文档的基本信息
                .select() // 开始选择要包含在文档中的API
                .apis(RequestHandlerSelectors.basePackage("com.app.login")) // 指定扫描的基础包路径
                .paths(PathSelectors.any()) // 匹配所有的路径
                .build(); // 构建Docket实例
    }

    // 定义一个方法，用于构建API文档的基本信息
    private ApiInfo apiInfo() {
        // 使用ApiInfoBuilder构建ApiInfo对象
        return new ApiInfoBuilder()
                .title("megadotnet use Swagger to build RESTful API") // 设置API文档的标题
                .description("megadotnet  https://github.com/megadotnet/") // 设置API文档的描述
                .termsOfServiceUrl("https://github.com/megadotnet/") // 设置服务条款的URL
                .contact("megadotnet") // 设置联系人信息
                .version("1.0") // 设置API文档的版本
                .build(); // 构建ApiInfo对象
    }
}
