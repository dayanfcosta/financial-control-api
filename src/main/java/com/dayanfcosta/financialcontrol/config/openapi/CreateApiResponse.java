package com.dayanfcosta.financialcontrol.config.openapi;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import com.dayanfcosta.financialcontrol.config.rest.HttpErrorResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.springframework.core.annotation.AliasFor;

@Retention(RUNTIME)
@Target({METHOD, ANNOTATION_TYPE})
@Operation
@ApiResponses(value = {
    @ApiResponse(responseCode = "201", description = "resource created"),
    @ApiResponse(responseCode = "401", description = "not authenticated request", content = @Content(schema = @Schema(implementation = HttpErrorResponse.class))),
    @ApiResponse(responseCode = "409", description = "duplicated resource", content = @Content(schema = @Schema(implementation = HttpErrorResponse.class))),
    @ApiResponse(responseCode = "500", description = "internal server error", content = @Content(schema = @Schema(implementation = HttpErrorResponse.class)))
})
public @interface CreateApiResponse {

  @AliasFor(annotation = Operation.class)
  String summary() default "";

}
