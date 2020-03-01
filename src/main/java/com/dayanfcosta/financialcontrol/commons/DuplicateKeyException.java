package com.dayanfcosta.financialcontrol.commons;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.dao.DataIntegrityViolationException;

/**
 * This annotation is responsible for customize message for {@link DataIntegrityViolationException}
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface DuplicateKeyException {

  String value() default "";

}