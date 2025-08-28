package pe.com.creditya.api.mapper;

import org.mapstruct.Mapper;
import pe.com.creditya.api.dtos.ApplicationRequest;
import pe.com.creditya.api.dtos.ApplicationResponse;
import pe.com.creditya.model.application.Application;

@Mapper(componentModel = "spring")
public interface ApplicationMapper {
    Application toApplication(ApplicationRequest request);
    ApplicationResponse toApplicationResponse(Application application);
}
