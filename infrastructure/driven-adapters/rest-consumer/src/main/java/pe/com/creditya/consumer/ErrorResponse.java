package pe.com.creditya.consumer;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse<T> {
    private List<T> errorResponseDto;
}
