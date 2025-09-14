package pe.com.creditya.model.application;

public record PaginationMetadata(int page, int size, long totalElements, int totalPages) {}