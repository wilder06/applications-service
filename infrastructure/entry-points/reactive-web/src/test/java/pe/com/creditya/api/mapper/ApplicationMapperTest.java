package pe.com.creditya.api.mapper;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import pe.com.creditya.api.dtos.ApplicationRequest;
import pe.com.creditya.api.dtos.ApplicationResponse;
import pe.com.creditya.model.application.Application;
import pe.com.creditya.model.loantype.LoanTypeEnum;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class ApplicationMapperTest {

    private final ApplicationMapper applicationMapper = Mappers.getMapper(ApplicationMapper.class);


    @Test
    void shouldMapRequestToApplication() {
        ApplicationRequest request = ApplicationRequest.builder()
                .documentNumber("12345678")
                .amount(new BigDecimal(1002))
                .term(12)
                .loanType(LoanTypeEnum.PERSONAL.name())
                .build();

        Application application = applicationMapper.toApplication(request);

        assertThat(application).isNotNull();
    }

    @Test
    void shouldMapApplicationToResponse() {
        Application application = Application.builder()
                .documentNumber("87654321")
                .email("mapped@example.com")
                .build();

        ApplicationResponse response = applicationMapper.toApplicationResponse(application);

        assertThat(response).isNotNull();
    }
}