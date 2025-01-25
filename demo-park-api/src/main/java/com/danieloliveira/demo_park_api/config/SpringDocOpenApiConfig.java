package com.danieloliveira.demo_park_api.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SpringDocOpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .components(new Components().addSecuritySchemes("security", securityScheme()))
                .info(new Info()
                        .title("REST API - Spring Park")
                        .description("Spring Park API Documentation")
                        .version("1.0")
                        .license(new License().name("Apache 2.0").url("https://www.apache.org/licenses/LICENSE-2.0"))
                        .contact(new Contact().name("Daniel Oliveira").email("daniel@spring-park.com")));
    }

    private SecurityScheme securityScheme() {
        return new SecurityScheme()
                .description("Insira um bearer token válido para prosseguir")
                .type(SecurityScheme.Type.HTTP)
                .in(SecurityScheme.In.HEADER)
                .scheme("Bearer")
                .bearerFormat("JWT")
                .name("security");
    }
}
