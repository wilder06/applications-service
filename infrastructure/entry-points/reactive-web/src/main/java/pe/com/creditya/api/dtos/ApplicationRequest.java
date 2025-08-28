package pe.com.creditya.api.dtos;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ApplicationRequest {
    @NotNull(message = "El monto no puede estar nulo")
    @Min(value = 0, message = "El monto no puede ser menor que 0")
    private BigDecimal amount;
    @NotNull(message = "El plazo no puede estar nulo")
    private Integer term;
    @NotBlank(message = "El Numero de Documento no puede estar vacío")
    private String documentNumber;
    @NotBlank(message = "El Tipo de Prestamo no puede estar vacío")
    private String loanType;
}
