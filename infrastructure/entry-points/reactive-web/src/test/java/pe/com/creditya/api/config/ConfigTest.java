package pe.com.creditya.api.config;

import org.junit.jupiter.api.BeforeEach;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import pe.com.creditya.api.Handler;
import pe.com.creditya.api.RouterRest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.reactive.server.WebTestClient;
import pe.com.creditya.api.common.config.ApplicationPath;
import pe.com.creditya.api.common.config.RequestValidator;
import pe.com.creditya.api.dtos.ApplicationRequest;
import pe.com.creditya.api.dtos.ApplicationResponse;
import pe.com.creditya.api.mapper.ApplicationMapper;
import pe.com.creditya.api.mapper.ApplicationMapperImpl;
import pe.com.creditya.model.application.Application;
import pe.com.creditya.usecase.application.ApplicationUseCase;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {RouterRest.class, Handler.class,
        ApplicationPath.class,
        RequestValidator.class, ApplicationMapperImpl.class})
@WebFluxTest
@Import({CorsConfig.class, SecurityHeadersConfig.class})
class ConfigTest {
    @Autowired
    private WebTestClient webTestClient;

    @MockitoBean
    private ApplicationMapper applicationMapper;

    @MockitoBean
    private ApplicationUseCase applicationUseCase;
Application application=Application.builder()
        .email("emal@gmail.com")
        .amount(BigDecimal.valueOf(1000))
        .documentNumber("48007099")
        .term(60)
        .idLoanType(1L)
        .build();

ApplicationResponse applicationResponse =ApplicationResponse.builder()
        .amount(BigDecimal.valueOf(1000))
        .term(60)
        .build();

    @BeforeEach
    void setUp() {
        when(applicationMapper.toApplication(any(ApplicationRequest.class))).thenReturn(application);
        when(applicationUseCase.saveLoanApplication(any(Application.class))).thenReturn(Mono.just(application));
        when(applicationMapper.toApplicationResponse(any(Application.class))).thenReturn(applicationResponse);

    }
    @Test
    void corsConfigurationShouldAllowOrigins() {
        webTestClient.post()
                .uri("/api/v1/solicitudes")
                .exchange()
                .expectHeader().valueEquals("Content-Security-Policy",
                        "default-src 'self'; frame-ancestors 'self'; form-action 'self'")
                .expectHeader().valueEquals("Strict-Transport-Security", "max-age=31536000;")
                .expectHeader().valueEquals("X-Content-Type-Options", "nosniff")
                .expectHeader().valueEquals("Server", "")
                .expectHeader().valueEquals("Cache-Control", "no-store")
                .expectHeader().valueEquals("Pragma", "no-cache")
                .expectHeader().valueEquals("Referrer-Policy", "strict-origin-when-cross-origin")
                .expectStatus().isOk();
    }

}