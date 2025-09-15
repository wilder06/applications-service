package pe.com.creditya.model.loanstatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import pe.com.creditya.model.loantype.LoanTypeEnum;

import java.util.Arrays;
import java.util.Objects;

@Getter
@AllArgsConstructor
public enum LoanStatusEnum {
    PENDING(1L, "PENDING"),
    APPROVED(2L, "APPROVED"),
    REJECTED(3L, "REJECTED"),
    CANCELLED(4L, "UNDER_REVIEW"),
    DISBURSED(5L, "CANCEL");
    private final Long id;
    private final String description;
    public static LoanStatusEnum fromId(Long id) {
        return Arrays.stream(values())
                .filter(s -> Objects.equals(s.id, id))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Estado no válido: " + id));
    }
    public static Long fromName(String name) {
        return Arrays.stream(values())
                .filter(e -> e.name().equalsIgnoreCase(name))
                .findFirst()
                .map(LoanStatusEnum::getId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid loan type: " + name));
    }
}
