package pe.com.creditya.model.user;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class User {
    private String documentNumber;
    private String email;
}
