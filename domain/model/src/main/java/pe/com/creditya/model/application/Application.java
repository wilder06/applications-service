package pe.com.creditya.model.application;
import lombok.Builder;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Application {
    private Integer idApplication;
    private BigDecimal amount;
    private Integer term;
    private String documentNumber;
    private String email;
    private Long idStatus;
    private Long idLoanType;
    private BigDecimal interestRate;
}