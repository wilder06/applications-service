package pe.com.creditya.model.loantype;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
@AllArgsConstructor
public enum LoanTypeEnum {
    PERSONAL(1L, "Préstamo Personal"),
    MORTGAGE(2L, "Hipotecario"),
    CAR(3L, "Vehicular"),
    EDUCATIONAL(4L, "Educativo"),
    QUICK_CONSUMPTION(5L, "Consumo Rápido");
    private final Long id;
    private final String description;


    public static Long fromName(String name) {
        return Arrays.stream(values())
                .filter(e -> e.name().equalsIgnoreCase(name))
                .findFirst()
                .map(LoanTypeEnum::getId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid loan type: " + name));
    }

    public static LoanTypeEnum fromId(Long id) {
        return Arrays.stream(values())
                .filter(e -> Objects.equals(e.id, id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid loan type id: " + id));
    }
}
