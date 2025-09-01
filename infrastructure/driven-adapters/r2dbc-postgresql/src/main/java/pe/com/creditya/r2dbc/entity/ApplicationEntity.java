package pe.com.creditya.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("loan_application")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ApplicationEntity {
    @Id
    @Column("id_application")
    private Integer id;
    private BigDecimal amount;
    private Integer term;
    private String email;
    private Long idStatus;
    private Long idLoanType;
}