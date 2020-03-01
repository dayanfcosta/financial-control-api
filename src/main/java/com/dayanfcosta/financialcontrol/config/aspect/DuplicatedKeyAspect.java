package com.dayanfcosta.financialcontrol.config.aspect;

import com.dayanfcosta.financialcontrol.commons.DuplicateKeyException;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class DuplicatedKeyAspect {

  @Around("@annotation(com.dayanfcosta.financialcontrol.commons.DuplicateKeyException)")
  public Object arroundDuplicatedKey(final ProceedingJoinPoint joinPoint) throws Throwable {
    try {
      return joinPoint.proceed();
    } catch (final DataIntegrityViolationException ex) {
      final var signature = (MethodSignature) joinPoint.getSignature();
      final var method = signature.getMethod();
      final var message = method.getAnnotation(DuplicateKeyException.class).value();
      throw new DataIntegrityViolationException(message);
    }
  }

}
