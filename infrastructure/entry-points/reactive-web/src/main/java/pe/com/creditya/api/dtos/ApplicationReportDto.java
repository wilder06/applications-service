package pe.com.creditya.api.dtos;

import java.math.BigDecimal;

public record ApplicationReportDto(
        BigDecimal amount,
        Integer term,
        String name,
        String email,
        BigDecimal baseSalary,
        String loanType,
        String loanStatus,
        BigDecimal interestRate,
        BigDecimal monthlyRequestAmount
) {
}
