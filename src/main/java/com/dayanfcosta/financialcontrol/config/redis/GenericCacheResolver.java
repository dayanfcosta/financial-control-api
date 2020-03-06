package com.dayanfcosta.financialcontrol.config.redis;

import com.dayanfcosta.financialcontrol.commons.AbstractRepository;
import com.google.common.base.CaseFormat;
import com.google.common.collect.ImmutableSet;
import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import org.springframework.cache.CacheManager;
import org.springframework.cache.interceptor.CacheOperationInvocationContext;
import org.springframework.cache.interceptor.SimpleCacheResolver;

public class GenericCacheResolver extends SimpleCacheResolver {

  public GenericCacheResolver(final CacheManager cacheManager) {
    super(cacheManager);
  }

  @Override
  protected Collection<String> getCacheNames(final CacheOperationInvocationContext<?> context) {
    final String className = cacheName(context);
    return ImmutableSet.of(CaseFormat.UPPER_CAMEL.to(CaseFormat.LOWER_UNDERSCORE, className));
  }

  private String cacheName(final CacheOperationInvocationContext<?> context) {
    final var repository = (AbstractRepository) context.getTarget();
    final var repositoryType = ((ParameterizedType) repository.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    return ((Class) repositoryType).getSimpleName();
  }
}
