package pe.com.creditya.api.dtos;

public record PageMetadataDto(int page,
                              int size,
                              long totalElements,
                              long totalPages) {
}
