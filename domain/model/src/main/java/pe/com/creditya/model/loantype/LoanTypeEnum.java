package pe.com.creditya.model.loantype;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum LoanTypeEnum {
    PERSONAL(1, "Préstamo Personal"),
    MORTGAGE(2, "Hipotecario"),
    CAR(3, "Vehicular"),
    EDUCATIONAL(4, "Educativo"),
    QUICK_CONSUMPTION(5, "Consumo Rápido");
    private int id;
    private String description;


    public static Integer fromName(String name) {
        return Arrays.stream(values())
                .filter(e -> e.name().equalsIgnoreCase(name))
                .findFirst()
                .map(LoanTypeEnum::getId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid loan type: " + name));
    }

    public static LoanTypeEnum fromId(int id) {
        return Arrays.stream(values())
                .filter(e -> e.id == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid loan type id: " + id));
    }
}
