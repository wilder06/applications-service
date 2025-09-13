package pe.com.creditya.model.user;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    private String email;
    private String name;
    private BigDecimal baseSalary;
}
