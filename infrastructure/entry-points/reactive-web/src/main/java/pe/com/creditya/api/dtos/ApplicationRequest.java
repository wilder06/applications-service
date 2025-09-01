package pe.com.creditya.api.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.math.BigDecimal;
@Builder
public record ApplicationRequest(
        @NotNull BigDecimal amount,
        @NotNull Integer term,
        @NotBlank String documentNumber,
        @NotBlank String loanType) {
}
