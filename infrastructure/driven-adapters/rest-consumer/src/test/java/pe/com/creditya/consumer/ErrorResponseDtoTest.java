package pe.com.creditya.consumer;

import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

class ErrorResponseDtoTest {

    @Test
    void shouldCreateErrorResponseDtoWithAllFields() {
        // given
        String code = "404";
        String message = "Not Found";
        Instant now = Instant.now();

        // when
        ErrorResponseDto dto = new ErrorResponseDto(code, message, now);

        // then
        assertThat(dto.code()).isEqualTo(code);
        assertThat(dto.message()).isEqualTo(message);
        assertThat(dto.timestamp()).isEqualTo(now);
    }

    @Test
    void shouldCreateErrorResponseDtoWithDefaultTimestamp() {
        // given
        String code = "500";
        String message = "Internal Server Error";

        // when
        ErrorResponseDto dto = new ErrorResponseDto(code, message);

        // then
        assertThat(dto.code()).isEqualTo(code);
        assertThat(dto.message()).isEqualTo(message);
        assertThat(dto.timestamp()).isNotNull();
        assertThat(dto.timestamp()).isBeforeOrEqualTo(Instant.now());
    }
}