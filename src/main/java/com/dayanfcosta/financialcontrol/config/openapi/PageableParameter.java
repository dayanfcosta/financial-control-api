package com.dayanfcosta.financialcontrol.config.openapi;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import org.springdoc.core.converters.PageableAsQueryParam;
import org.springframework.core.annotation.AliasFor;

@Retention(RUNTIME)
@Target({METHOD, ANNOTATION_TYPE})
@QueryApiResponse
@PageableAsQueryParam
public @interface PageableParameter {

  @AliasFor(annotation = QueryApiResponse.class)
  String summary() default "";

}
