package com.ims.inventory.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "My API", version = "1.0"),
        security = @SecurityRequirement(name = "Authorization") // Applies to all APIs
)
@SecuritySchemes({
        @SecurityScheme(
                name = "Authorization",
                type = SecuritySchemeType.APIKEY,
                scheme = "bearer",
                bearerFormat = "JWT",
                in = io.swagger.v3.oas.annotations.enums.SecuritySchemeIn.HEADER
        )
})
public class SwaggerConfig {

        // Customize API info (optional)
        @Bean
        public OpenAPI customOpenAPI() {
                return new OpenAPI()
                        .info(new io.swagger.v3.oas.models.info.Info()
                                .title("Inventory API")
                                .version("1.0")
                                .description("Only your custom APIs"));
        }

        // Grouped OpenAPI to limit package scanning
        @Bean
        public GroupedOpenApi myInventoryApi() {
                return GroupedOpenApi.builder()
                        .group("inventory") // Just a label
                        .packagesToScan("com.ims.inventory.controller") // <== change to your controller package
                        .build();
        }

}
