package me.zeph.spring.data.redis.config;

import me.zeph.spring.data.redis.model.Student;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
public class RedisConfig {

  @Bean
  public RedisTemplate<String, Student> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<String, Student> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(redisConnectionFactory);
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Student.class));

    redisTemplate.setEnableTransactionSupport(true);

    return redisTemplate;
  }
}
