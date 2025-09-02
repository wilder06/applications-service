package pe.com.creditya.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import pe.com.creditya.api.dtos.ApplicationRequest;
import pe.com.creditya.api.dtos.ApplicationResponse;
import pe.com.creditya.model.application.Application;
import pe.com.creditya.model.loanstatus.LoanStatuEnum;
import pe.com.creditya.model.loantype.LoanType;
import pe.com.creditya.model.loantype.LoanTypeEnum;

@Mapper(componentModel = "spring")
public interface ApplicationMapper {
    @Mapping(target = "idApplication", ignore = true)
    @Mapping(target = "email", ignore = true)
    @Mapping(target = "idStatus", ignore = true)
    @Mapping(target = "idLoanType",source ="request",qualifiedByName="getLoanTypeId")
    Application toApplication(ApplicationRequest request);

    @Mapping(target = "loanType",source ="application",qualifiedByName="getLoanTypeName")
    @Mapping(target = "loanStatus",source ="application",qualifiedByName="getLoanStatusName")
    ApplicationResponse toApplicationResponse(Application application);

    @Named("getLoanTypeId")
    default Long getLoanType(ApplicationRequest request){
        return LoanTypeEnum.fromName(request.loanType());
    }
    @Named("getLoanTypeName")
    default String getLoanTypeName(Application application){
        return LoanTypeEnum.fromId(application.getIdLoanType()).name();
    }
    @Named("getLoanStatusName")
    default String getLoanStatusName(Application application){
        return LoanStatuEnum.fromId(application.getIdStatus()).name();
    }
}
