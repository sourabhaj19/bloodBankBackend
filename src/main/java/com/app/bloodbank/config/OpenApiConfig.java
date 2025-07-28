package com.app.bloodbank.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI bloodBankOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Blood Bank API")
                        .description("API documentation for Blood Bank Management System")
                        .version("1.0"));
    }
}