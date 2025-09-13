package pe.com.creditya.consumer.dto;


import lombok.Builder;

import java.math.BigDecimal;
@Builder(toBuilder = true)
public record UserResponse(String email,
                           String name,
                           BigDecimal baseSalary) {

}