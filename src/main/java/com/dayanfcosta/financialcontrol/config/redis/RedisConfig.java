package com.dayanfcosta.financialcontrol.config.redis;

import java.time.Duration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {

  public static final String DEFAULT_CACHE_RESOLVER = "defaultCacheResolver";

  @Bean
  RedisTemplate<Object, Object> redisTemplate(final RedisConnectionFactory connectionFactory) {
    final var template = new RedisTemplate<>();
    template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer());
    template.setHashKeySerializer(new GenericJackson2JsonRedisSerializer());
    template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    template.setKeySerializer(new StringRedisSerializer());
    template.setConnectionFactory(connectionFactory);
    return template;
  }

  @Bean
  CacheManager redisCacheManager(final RedisConnectionFactory connectionFactory) {
    final RedisSerializationContext.SerializationPair<Object> jsonSerializer =
        RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer());
    return RedisCacheManager.RedisCacheManagerBuilder
        .fromConnectionFactory(connectionFactory)
        .cacheDefaults(cacheDefaultConfiguration(jsonSerializer))
        .build();
  }

  @Bean(DEFAULT_CACHE_RESOLVER)
  public CacheResolver cacheResolver(final CacheManager cacheManager) {
    return new GenericCacheResolver(cacheManager);
  }

  private RedisCacheConfiguration cacheDefaultConfiguration(final SerializationPair<Object> jsonSerializer) {
    return RedisCacheConfiguration.defaultCacheConfig()
        .entryTtl(Duration.ofDays(1))
        .serializeValuesWith(jsonSerializer);
  }

}