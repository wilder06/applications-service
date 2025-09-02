package pe.com.creditya.model.loanstatus;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Objects;

@Getter
@AllArgsConstructor
public enum LoanStatuEnum {
    PENDING(1L, "PENDING"),
    APPROVED(2L, "APPROVED"),
    REJECTED(3L, "REJECTED"),
    CANCELLED(4L, "CANCELLED"),
    DISBURSED(5L, "DISBURSED");
    private  Long id;
    private  String description;
    public static LoanStatuEnum fromId(Long id) {
        return Arrays.stream(values())
                .filter(s -> Objects.equals(s.id, id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Estado no válido: " + id));
    }
}
