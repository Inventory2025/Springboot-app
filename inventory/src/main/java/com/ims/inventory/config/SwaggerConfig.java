package com.ims.inventory.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
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

        @Bean
        public boolean enableSwagger(Environment environment) {
                return environment.acceptsProfiles("local");
        }

}
