package org.employeeservice.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPI30Configuration {

    @Bean
    public OpenAPI customizeOpenAPI() {

        return new OpenAPI()
                .info(new Info().title("Employee Management Service")
                        .description("This is a sample employee management service API")
                        .version("1.0.0"));
    }
}
