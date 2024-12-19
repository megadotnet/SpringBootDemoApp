package com.app.login.config;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;
import com.app.login.security.jwt.JWTConfigurer;
import com.app.login.security.jwt.TokenProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;


/**
 * Updated SecurityConfiguration for Spring Boot 3
 * @author Megadotnet
 * @date 2024-03-07
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration {

    private final UserDetailsService userDetailsService;
    private final TokenProvider tokenProvider;
    private final CorsFilter corsFilter;
    private final AuthenticationConfiguration authenticationConfiguration;

    public SecurityConfiguration(
            UserDetailsService userDetailsService,
            TokenProvider tokenProvider,
            CorsFilter corsFilter,
            AuthenticationConfiguration authenticationConfiguration) {
        this.userDetailsService = userDetailsService;
        this.tokenProvider = tokenProvider;
        this.corsFilter = corsFilter;
        this.authenticationConfiguration = authenticationConfiguration;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Configure JWT security filter
        http.addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class);

        // Configure security settings
        http
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint((request, response, ex) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED)))
                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.disable()))
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/api/register").permitAll()
                        .requestMatchers("/api/activate").permitAll()
                        .requestMatchers("/api/authenticate").permitAll()
                        .requestMatchers("/api/account/reset_password/init").permitAll()
                        .requestMatchers("/api/account/reset_password/finish").permitAll()
                        .requestMatchers("/app/**").permitAll()
                        .requestMatchers("/bower_components/**").permitAll()
                        .requestMatchers("/i18n/**").permitAll()
                        .requestMatchers("/content/**").permitAll()
                        .requestMatchers("/test/**").permitAll()
                        .requestMatchers("/h2-console/**").permitAll()
                        .requestMatchers("/api/**").authenticated()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers("/v3/api-docs/**").permitAll() // Swagger API 文档
                        .requestMatchers("/swagger-ui.html").permitAll() // Swagger UI 页面
                        .requestMatchers("/swagger-ui/**").permitAll() // Swagger UI 相关资源
                        .requestMatchers("/static/**").permitAll() // 静态文件
                        .requestMatchers("/**").permitAll())
                .apply(new JWTConfigurer(tokenProvider));

        return http.build();
    }

    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}