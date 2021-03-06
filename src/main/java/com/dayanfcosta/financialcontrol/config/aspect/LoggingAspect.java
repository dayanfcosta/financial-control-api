package com.dayanfcosta.financialcontrol.config.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Aspect
@Configuration
@EnableAspectJAutoProxy
public class LoggingAspect {

  private static final Logger LOGGER = LoggerFactory.getLogger(LoggingAspect.class);

  @Around("execution(* com.dayanfcosta..*(..)))")
  public Object profileAllMethods(final ProceedingJoinPoint joinPoint) throws Throwable {
    final MethodSignature signature = (MethodSignature) joinPoint.getSignature();
    final String className = signature.getDeclaringType().getName();
    final String method = signature.getName();
    if (joinPoint.getArgs().length > 0) {
      LOGGER.debug("Executing {}.{} with following arguments: {}}", className, method, joinPoint.getArgs());
    } else {
      LOGGER.debug("Executing {}.{}}", className, method);
    }
    return joinPoint.proceed();
  }

  @AfterThrowing(pointcut = "execution(* com.dayanfcosta..*(..))", throwing = "ex")
  public void logAfterThrowingAllMethods(final JoinPoint joinPoint, final Exception ex) throws Throwable {
    final var signature = joinPoint.getSignature();
    final var className = signature.getDeclaringType().getName();
    LOGGER.error("Error when executing {}.{} with arguments: {} ", className, signature.getName(), joinPoint.getArgs());
  }

}
