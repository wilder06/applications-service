package pe.com.creditya.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pe.com.creditya.api.dtos.*;
import pe.com.creditya.model.application.Application;
import pe.com.creditya.model.application.ApplicationReport;
import pe.com.creditya.model.application.PaginatedApplication;
import pe.com.creditya.model.application.PaginationMetadata;
import pe.com.creditya.model.loanstatus.LoanStatusEnum;
import pe.com.creditya.model.loantype.LoanTypeEnum;

@Mapper(componentModel = "spring")
public interface ApplicationMapper {
    @Mapping(target = "idApplication", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "idStatus", ignore = true)
    @Mapping(target = "idLoanType",source ="request",qualifiedByName="getLoanTypeId")
    Application toApplication(ApplicationRequest request);

    @Mapping(target = "loanType",source ="application.idLoanType",qualifiedByName="getLoanTypeName")
    @Mapping(target = "loanStatus",source ="application.idStatus",qualifiedByName="getLoanStatusName")
    ApplicationResponse toApplicationResponse(Application application);

    @Mapping(target = "loanType",source ="report.idLoanType",qualifiedByName="getLoanTypeName")
    @Mapping(target = "loanStatus",source ="report.idStatus",qualifiedByName="getLoanStatusName")
    ApplicationReportDto toDto(ApplicationReport report);
    PageMetadataDto toDto(PaginationMetadata metadata);

    default PaginatedResponseDto<ApplicationReportDto> toDto(PaginatedApplication<ApplicationReport> response) {
        return PaginatedResponseDto.<ApplicationReportDto>builder()
                .data(response.data().stream().map(this::toDto).toList())
                .metadata(toDto(response.metadata()))
                .build();
    }
    @Named("getLoanTypeId")
    default Long getLoanType(ApplicationRequest request){
        return LoanTypeEnum.fromName(request.loanType());
    }

    @Named("getLoanTypeName")
    default String getLoanTypeName(Long idLoanType){
        return LoanTypeEnum.fromId(idLoanType).name();
    }
    @Named("getLoanStatusName")
    default String getLoanStatusName(Long idStatus){
        return LoanStatusEnum.fromId(idStatus).name();
    }
}
