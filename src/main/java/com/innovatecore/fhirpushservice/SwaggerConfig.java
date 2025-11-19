package com.innovatecore.fhirpushservice;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("FHIR Facility Uploader API")
                        .version("1.0")
                        .description("API for uploading healthcare facilities from CSV to FHIR server")
                        .contact(new Contact()
                                .name("Healthcare System")
                                .email("support@healthcare.com")));
    }
}