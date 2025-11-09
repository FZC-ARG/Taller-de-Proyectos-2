package com.appmartin.desmartin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.lang.NonNull;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AuditoriaWebConfig implements WebMvcConfigurer {

    private final AuditoriaRequestInterceptor auditoriaRequestInterceptor;

    public AuditoriaWebConfig(AuditoriaRequestInterceptor auditoriaRequestInterceptor) {
        this.auditoriaRequestInterceptor = auditoriaRequestInterceptor;
    }

    @Override
    public void addInterceptors(@NonNull InterceptorRegistry registry) {
        registry.addInterceptor(auditoriaRequestInterceptor)
            .addPathPatterns("/**")
            .excludePathPatterns(
                "/error",
                "/swagger-ui/**",
                "/v3/api-docs/**",
                "/webjars/**",
                "/actuator/**",
                "/favicon.ico"
            );
    }
}

