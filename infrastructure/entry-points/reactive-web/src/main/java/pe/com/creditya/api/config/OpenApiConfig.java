package pe.com.creditya.api.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI taskServiceOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Loan Application Service API")
                        .description("Microservicio de registro de Solicitudes prestamo CreditYa")
                        .version("v1.0.0")
                        .license(new License().name("Apache 2.0").url("http://springdoc.org")));
    }
}