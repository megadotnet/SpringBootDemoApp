package com.app.login.config;

import org.springframework.boot.actuate.autoconfigure.endpoint.web.CorsEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.endpoint.web.WebEndpointProperties;
import org.springframework.boot.actuate.autoconfigure.web.server.ManagementPortType;
import org.springframework.boot.actuate.endpoint.ExposableEndpoint;
import org.springframework.boot.actuate.endpoint.web.*;
import org.springframework.boot.actuate.endpoint.web.annotation.ControllerEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.annotation.ServletEndpointsSupplier;
import org.springframework.boot.actuate.endpoint.web.servlet.WebMvcEndpointHandlerMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * SpringFoxConfig
 * @author megadotnet
 * @date 2024/10/31
 * for springboot 2.7.15与springfox-swagger-ui 2.8.0, 启动有如下异常，如何解决：Caused by: java.lang.NullPointerException
 */
@Configuration
public class SpringFoxConfig {

    /**
     * 配置并返回一个用于处理Web端点请求的映射器
     * 该方法主要用于Spring Boot应用中，以支持通过HTTP请求访问应用的端点（Endpoint）
     * 这对于监控和管理应用非常有用，例如查看应用的健康状况、获取环境属性等
     *
     * @param webEndpointProperties 包含Web端点属性的配置对象，如路径、敏感信息等
     * @param securityRolesMapper 一个函数式接口，用于将从请求中提取的角色映射到实际的角色
     * @param webEndpoints 提供要暴露的Web端点列表
     * @param endpointMediaTypes 支持的媒体类型列表，用于端点的输入和输出
     * @param corsConfigurations 跨域资源共享（CORS）配置的源，允许配置CORS策略
     * @param endpointHandler 针对端点请求的处理器，负责实际处理请求
     * @return 返回一个配置好的WebMvcEndpointHandlerMapping对象，用于处理Web端点请求
     */
    @Bean
    public WebMvcEndpointHandlerMapping webEndpointServletHandlerMapping(
            WebEndpointsSupplier webEndpointsSupplier,
            ServletEndpointsSupplier servletEndpointsSupplier,
            ControllerEndpointsSupplier controllerEndpointsSupplier,
            EndpointMediaTypes endpointMediaTypes,
            CorsEndpointProperties corsProperties,
            WebEndpointProperties webEndpointProperties,
            Environment environment) {
        List<ExposableEndpoint<?>> allEndpoints = new ArrayList<>();
        Collection<ExposableWebEndpoint> webEndpoints = webEndpointsSupplier.getEndpoints();
        allEndpoints.addAll(webEndpoints);
        allEndpoints.addAll(servletEndpointsSupplier.getEndpoints());
        allEndpoints.addAll(controllerEndpointsSupplier.getEndpoints());
        String basePath = webEndpointProperties.getBasePath();
        EndpointMapping endpointMapping = new EndpointMapping(basePath);
        boolean shouldRegisterLinksMapping = shouldRegisterLinksMapping(webEndpointProperties, environment, basePath);
        return new WebMvcEndpointHandlerMapping(endpointMapping, webEndpoints, endpointMediaTypes,
                corsProperties.toCorsConfiguration(), new EndpointLinksResolver(allEndpoints, basePath),
                shouldRegisterLinksMapping, null);
    }

    private boolean shouldRegisterLinksMapping(WebEndpointProperties webEndpointProperties, Environment environment,
                                               String basePath) {
        return webEndpointProperties.getDiscovery().isEnabled() && (StringUtils.hasText(basePath)
                || ManagementPortType.get(environment).equals(ManagementPortType.DIFFERENT));
    }
}