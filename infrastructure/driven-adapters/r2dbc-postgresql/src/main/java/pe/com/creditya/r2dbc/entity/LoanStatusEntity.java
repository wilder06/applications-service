package pe.com.creditya.r2dbc.entity;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("loan_status")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class LoanStatusEntity {
    @Id
    @Column("id_status")
    private Long id;
    private String  name;
    private String description;
}
