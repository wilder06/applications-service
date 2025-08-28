package pe.com.creditya.consumer;

import java.time.Instant;

public record ErrorResponseDto(String code,
                               String message,
                               Instant timestamp) {
    public ErrorResponseDto(String code, String message) {
        this(code, message, Instant.now());
    }
}