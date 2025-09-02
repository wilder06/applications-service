package pe.com.creditya.api;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import pe.com.creditya.api.common.config.ApplicationPath;
import pe.com.creditya.api.common.config.RequestValidator;
import pe.com.creditya.api.dtos.ApplicationRequest;
import pe.com.creditya.api.dtos.ApplicationResponse;
import pe.com.creditya.api.mapper.ApplicationMapper;
import pe.com.creditya.api.mapper.ApplicationMapperImpl;
import pe.com.creditya.model.application.Application;
import pe.com.creditya.model.loanstatus.LoanStatuEnum;
import pe.com.creditya.model.loantype.LoanTypeEnum;
import pe.com.creditya.usecase.application.ApplicationUseCase;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ContextConfiguration(classes = {RouterRest.class, Handler.class, RequestValidator.class, ApplicationMapperImpl.class})
@EnableConfigurationProperties(ApplicationPath.class)
@WebFluxTest
class RouterRestTest {

    @Autowired
    private WebTestClient webTestClient;
    @MockitoBean
    private ApplicationMapper applicationMapper;

    @MockitoBean
    private ApplicationUseCase applicationUseCase;
    @Autowired
    private ApplicationPath applicationPath;

    Application application=Application.builder()
            .email("emal@gmail.com")
            .amount(BigDecimal.valueOf(1000))
            .documentNumber("48107091")
            .term(60)
            .idLoanType(1L)
            .build();

    ApplicationResponse applicationResponse =ApplicationResponse.builder()
            .amount(BigDecimal.valueOf(1000))
            .term(60)
            .loanStatus(LoanStatuEnum.PENDING.name())
            .loanType(LoanTypeEnum.PERSONAL.name())
            .build();
    ApplicationRequest applicationRequest=ApplicationRequest.builder()
            .amount(BigDecimal.valueOf(1000))
            .documentNumber("43107034")
            .term(60)
            .loanType(LoanTypeEnum.PERSONAL.name())
            .build();

    @Test
    void shouldLoadUserPathProperties() {
        assertEquals("/api/v1/solicitudes", applicationPath.getApplications());
    }
    @Test
    void testListenPOSTUseCase() {
        when(applicationMapper.toApplication(any(ApplicationRequest.class))).thenReturn(application);
        when(applicationUseCase.saveLoanApplication(any(Application.class))).thenReturn(Mono.just(application));
        when(applicationMapper.toApplicationResponse(any(Application.class))).thenReturn(applicationResponse);
        webTestClient.post()
                .uri(applicationPath.getApplications())
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(applicationRequest)
                .exchange()
                .expectStatus().isCreated();
    }

}
