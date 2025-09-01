package pe.com.creditya.model.loanstatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum LoanStatuEnum {
    PENDING(1, "Pendiente"),
    APPROVED(2, "Aprobado"),
    REJECTED(3, "Rechazado");
    private  int id;
    private  String description;
    public static LoanStatuEnum fromId(int id) {
        return Arrays.stream(values())
                .filter(s -> s.id == id)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Estado no válido: " + id));
    }
}
