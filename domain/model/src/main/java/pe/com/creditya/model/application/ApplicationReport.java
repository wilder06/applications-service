package pe.com.creditya.model.application;

import lombok.*;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class ApplicationReport {
    private BigDecimal amount;
    private Integer term;
    private String email;
    private String name;
    private Long idLoanType;
    private BigDecimal interestRate;
    private Long idStatus;
    private BigDecimal baseSalary;
    private BigDecimal monthlyRequestAmount;
}
