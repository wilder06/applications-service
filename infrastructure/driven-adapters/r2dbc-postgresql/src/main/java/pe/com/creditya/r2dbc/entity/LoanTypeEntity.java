package pe.com.creditya.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;

@Table("loan_type")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LoanTypeEntity {
    @Id
    @Column("id_loan_type")
    private Long id;
    private String name;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private Integer interestRate;
    private Boolean automaticValidation;
}