package pe.com.creditya.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import pe.com.creditya.security.constants.ApiPaths;
import pe.com.creditya.security.repository.SecurityContextRepository;

import static pe.com.creditya.security.constants.ApiPaths.APPLICATIONS_PATH;

@Configuration
@EnableWebFluxSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final SecurityContextRepository securityContextRepository;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .securityContextRepository(securityContextRepository)
                .authorizeExchange(exchange -> exchange
                        .pathMatchers(ApiPaths.API_DOCS_ALL,
                                ApiPaths.SWAGGER_UI,
                                ApiPaths.SWAGGER_UI_ALL,
                                ApiPaths.WEBJARS_ALL,
                                ApiPaths.SWAGGER_RESOURCES_ALL).permitAll()
                        .pathMatchers(ApiPaths.HEALTH_CHECK).permitAll()
                        .pathMatchers(HttpMethod.POST,APPLICATIONS_PATH).authenticated()
                        .pathMatchers(HttpMethod.GET,APPLICATIONS_PATH).authenticated()
                        .anyExchange().authenticated()
                )
                .build();
    }
}