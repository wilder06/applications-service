package pe.com.creditya.api.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import pe.com.creditya.api.dtos.ApplicationRequest;
import pe.com.creditya.api.dtos.ApplicationResponse;
import pe.com.creditya.model.application.Application;

@Mapper(componentModel = "spring")
public interface ApplicationMapper {
    @Mapping(target = "idApplication", ignore = true)
    @Mapping(target = "email", ignore = true)
    Application toApplication(ApplicationRequest request);

    ApplicationResponse toApplicationResponse(Application application);
}
