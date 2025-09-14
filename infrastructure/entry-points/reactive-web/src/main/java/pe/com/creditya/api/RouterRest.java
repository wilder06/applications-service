package pe.com.creditya.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;
import pe.com.creditya.api.common.config.ApplicationPath;
import pe.com.creditya.api.dtos.ApplicationRequest;
import pe.com.creditya.api.dtos.ApplicationResponse;
import pe.com.creditya.api.dtos.ErrorResponseDto;
import pe.com.creditya.api.dtos.PaginatedResponseDto;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@RequiredArgsConstructor
@Configuration
public class RouterRest {
    private final ApplicationPath applicationPath;
    private final Handler applicationHandler;

    @Bean
    @RouterOperations({
            @RouterOperation(
                    path = "/api/v1/solicitudes",
                    method = RequestMethod.POST,
                    beanClass = Handler.class,
                    beanMethod = "listenSaveLoanApplication",
                    operation = @Operation(
                            operationId = "listenSaveLoanApplication",
                            security = @SecurityRequirement(name = "bearerAuth"),
                            summary = "Registrar nuevo Solicitud",
                            requestBody = @RequestBody(
                                    required = true,
                                    content = @Content(schema = @Schema(implementation = ApplicationRequest.class))
                            ),
                            responses = {
                                    @ApiResponse(
                                            responseCode = "201",
                                            description = "Solicitud de prestamo registrado correctamente",
                                            content = @Content(
                                                    schema = @Schema(implementation = ApplicationResponse.class),
                                                    mediaType = "application/json"
                                            )
                                    ),
                                    @ApiResponse(responseCode = "400",
                                            description = "Datos inválidos",
                                            content = @Content(
                                                    schema = @Schema(implementation = ErrorResponseDto.class),
                                                    mediaType = "application/json"
                                            )
                                    ),
                                    @ApiResponse(responseCode = "409",
                                            description = "Solicitud ya existe en proceso para el el cliente",
                                            content = @Content(
                                                    schema = @Schema(implementation = ErrorResponseDto.class),
                                                    mediaType = "application/json"
                                            )
                                    ),
                                    @ApiResponse(responseCode = "400",
                                            description = "Not Found",
                                            content = @Content(
                                                    schema = @Schema(implementation = ErrorResponseDto.class),
                                                    mediaType = "application/json"
                                            )
                                    ), @ApiResponse(responseCode = "404",
                                    description = "Bad Request",
                                    content = @Content(
                                            schema = @Schema(implementation = ErrorResponseDto.class),
                                            mediaType = "application/json"
                                    )
                            ),
                                    @ApiResponse(responseCode = "500",
                                            description = "Error general",
                                            content = @Content(
                                                    schema = @Schema(implementation = ErrorResponseDto.class),
                                                    mediaType = "application/json"
                                            )
                                    )

                            }
                    )
            ), @RouterOperation(
            path = "/api/v1/solicitudes",
            produces = {"application/json"},
            method = {RequestMethod.GET},
            beanClass = Handler.class,
            beanMethod = "listenAllLoanApplicationFindStatusPending",
            operation = @Operation(
                    operationId = "listenAllLoanApplicationFindStatusPending",
                    summary = "Obtener solicitudes por estado con paginación",
                    security = @SecurityRequirement(name = "bearerAuth"),
                    description = "Retorna todas las solicitudes con el estado especificado en formato paginado.",
                    parameters = {
                            @Parameter(name = "status", description = "Nombre del estado a filtrar", required = true, example = "PENDING"),
                            @Parameter(name = "page", description = "Número de página (0..N)", required = true, example = "0"),
                            @Parameter(name = "size", description = "Tamaño de página", required = true, example = "10")
                    },
                    responses = {
                            @ApiResponse(
                                    responseCode = "200",
                                    description = "Lista paginada de solicitudes",
                                    content = @Content(
                                            mediaType = "application/json",
                                            schema = @Schema(implementation = PaginatedResponseDto.class)
                                    )
                            ),
                            @ApiResponse(responseCode = "400", description = "Parámetros inválidos"),
                            @ApiResponse(responseCode = "500", description = "Error interno del servidor")
                    }
            )
    )
    })
    public RouterFunction<ServerResponse> routerFunction() {
        return route(POST(applicationPath.getApplications()), applicationHandler::listenSaveLoanApplication)
                .andRoute(GET(applicationPath.getApplications()), applicationHandler::listenAllLoanApplicationFindStatusPending);
    }
}
