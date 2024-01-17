package com.eddie.credit.application.system;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class configurationSwagger3Config<fun> {
    @Bean
    fun publicApi(): GroupedOpenApi? {
        return GroupedOpenApi.builder()
                .group("springcreditapplicationsystem-public")
                .pathsToMatch("/api/customers/**", "/api/credit/**")
                .build()
    }
}
