package com.app.login.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.data.repository.query.SecurityEvaluationContextExtension;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.CorsFilter;

import com.app.login.security.jwt.JWTConfigurer;
import com.app.login.security.jwt.TokenProvider;

/**
 * Updated SecurityConfiguration for Spring Boot 2.7+
 * @author Megadotnet
 * @date 2024-03-07
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
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
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // Configure JWT security filter
        http.addFilterBefore(corsFilter, UsernamePasswordAuthenticationFilter.class);

        // Configure security settings
        http
                .csrf()
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                .and()
                .headers()
                .frameOptions()
                .disable()
                .and()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .authorizeRequests()
                // Public endpoints
                .antMatchers("/api/register").permitAll()
                .antMatchers("/api/activate").permitAll()
                .antMatchers("/api/authenticate").permitAll()
                .antMatchers("/api/account/reset_password/init").permitAll()
                .antMatchers("/api/account/reset_password/finish").permitAll()
                // Static resources
                .antMatchers("/app/**/*.{js,html}").permitAll()
                .antMatchers("/bower_components/**").permitAll()
                .antMatchers("/i18n/**").permitAll()
                .antMatchers("/content/**").permitAll()
                .antMatchers("/test/**").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                // API endpoints
                .antMatchers("/api/**").authenticated()
                // Options
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .and()
                .apply(new JWTConfigurer(tokenProvider));

        return http.build();
    }

    @Bean
    public SecurityEvaluationContextExtension securityEvaluationContextExtension() {
        return new SecurityEvaluationContextExtension();
    }
}