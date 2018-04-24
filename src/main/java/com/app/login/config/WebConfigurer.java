package com.app.login.config;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import javax.servlet.DispatcherType;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import java.io.File;
import java.nio.file.Paths;
import java.util.EnumSet;

/**
 * Configuration of web application with Servlet 3.0 APIs.
 * @author Megadotnet
 * @date 2018-03-07
 * it also can be reference https://github.com/spring-projects/spring-boot/tree/master/spring-boot-samples
 */
@Configuration
@Slf4j
public class WebConfigurer implements ServletContextInitializer {

    private final Environment env;

    public WebConfigurer(Environment env) {
        this.env = env;
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        if (env.getActiveProfiles().length != 0) {
            log.info("Web application configuration, using profiles: {}", (Object[]) env.getActiveProfiles());
        }
        EnumSet<DispatcherType> disps = EnumSet.of(DispatcherType.REQUEST, DispatcherType.FORWARD, DispatcherType.ASYNC);
        initH2Console(servletContext);
        log.info("Web application fully configured");
    }

    /**
     *  Resolve path prefix to static resources.
     */
    private String resolvePathPrefix() {
        String fullExecutablePath = this.getClass()
            .getResource("")
            .getPath();
        String rootPath = Paths.get(".")
            .toUri()
            .normalize()
            .getPath();
        String extractedPath = fullExecutablePath.replace(rootPath, "");
        int extractionEndIndex = extractedPath.indexOf("target/");
        if (extractionEndIndex <= 0) {
            return "";
        }
        return extractedPath.substring(0, extractionEndIndex);
    }

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        if (config.getAllowedOrigins() != null && !config.getAllowedOrigins()
            .isEmpty()) {
            log.debug("Registering CORS filter");
            source.registerCorsConfiguration("/api/**", config);
        }
        return new CorsFilter(source);
    }

    /**
     * Initializes H2 console.
     */
    private void initH2Console(ServletContext servletContext) {
        log.debug("Initialize H2 console");
        ServletRegistration.Dynamic h2ConsoleServlet = servletContext.addServlet("H2Console", new org.h2.server.web.WebServlet());
        h2ConsoleServlet.addMapping("/h2-console/*");
        h2ConsoleServlet.setInitParameter("-properties", "src/main/resources/");
        h2ConsoleServlet.setLoadOnStartup(1);
    }
}
