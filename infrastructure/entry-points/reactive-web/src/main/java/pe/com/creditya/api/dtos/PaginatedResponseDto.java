package pe.com.creditya.api.dtos;

import lombok.Builder;

import java.util.List;
@Builder(toBuilder = true)
public record PaginatedResponseDto<T>(List<ApplicationReportDto> data,
                                      PageMetadataDto metadata) {
}
