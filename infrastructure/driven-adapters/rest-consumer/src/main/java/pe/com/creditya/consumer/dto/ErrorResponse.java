package pe.com.creditya.consumer.dto;

import java.util.List;

public record ErrorResponse<T>(
        List<T> errorResponseDto
) {
}