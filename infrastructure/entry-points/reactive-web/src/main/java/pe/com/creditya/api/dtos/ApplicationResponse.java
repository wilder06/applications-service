package pe.com.creditya.api.dtos;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ApplicationResponse(
         BigDecimal amount,
         Integer term,
         BigDecimal interestRate,
         String loanType,
         String loanStatus
) {
}
