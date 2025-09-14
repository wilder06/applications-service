package pe.com.creditya.consumer.dto;

import lombok.Builder;

import java.time.Instant;
@Builder
public record ErrorResponseDto(String code,
                               String message,
                               Instant timestamp) {
    public ErrorResponseDto(String code, String message) {
        this(code, message, Instant.now());
    }
}