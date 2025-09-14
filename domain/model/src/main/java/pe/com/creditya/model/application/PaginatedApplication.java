package pe.com.creditya.model.application;

import lombok.Builder;

import java.util.List;
@Builder(toBuilder = true)
public record PaginatedApplication<T>(List<T> data, PaginationMetadata metadata) {}
