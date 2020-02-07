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
    LOGGER.debug("Executing {}.{} with following arguments: {}}", className, method, joinPoint.getArgs());
    final Object result = joinPoint.proceed();
    return result;
  }

  @AfterThrowing(pointcut = "execution(* com.dayanfcosta..*(..))", throwing = "ex")
  public void logAfterThrowingAllMethods(final JoinPoint joinPoint, final Exception ex) throws Throwable {
    LOGGER.error("Error when executing {} with arguments: {} ", joinPoint.getSignature().getName(), joinPoint.getArgs(), ex);
  }

}
