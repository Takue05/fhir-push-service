package com.innovatecore.fhirpushservice;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import ca.uhn.fhir.rest.client.interceptor.BasicAuthInterceptor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FhirConfig {

    @Value("${fhir.server.url}")
    private String fhirServerUrl;

//    @Value("${fhir.server.username}")
//    private String username;
////
//    @Value("${fhir.server.password}")
//    private String password;

    @Bean
    public FhirContext fhirContext() {
        return FhirContext.forR4();
    }

    @Bean
    public IGenericClient fhirClient(FhirContext fhirContext) {
        IGenericClient client = fhirContext.newRestfulGenericClient(fhirServerUrl);

//        // Add basic authentication
//        BasicAuthInterceptor authInterceptor = new BasicAuthInterceptor(username, password);
//        client.registerInterceptor(authInterceptor);

        return client;
    }
}
