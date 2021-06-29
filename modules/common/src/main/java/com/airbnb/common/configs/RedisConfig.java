package com.airbnb.common.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Date;

/**
 * @author mir00r on 28/6/21
 * @project IntelliJ IDEA
 */
@Configuration
//@EnableRedisRepositories
@EnableCaching
public class RedisConfig {

    public static final String cacheName = "rental_search_cache";

//    @Value("${redisCentralCachingURL}")
//    private String redisHost;
//
//    @Value("${redisCentralCachingPort}")
//    private int redisPort;

    @CacheEvict(allEntries = true, value = {cacheName})
    @Scheduled(fixedDelay = 24 * 60 * 60 * 1000, initialDelay = 500)
    public void reportCacheEvict() {
        System.out.println("Flush Cache " + new Date());
    }

//    @Bean
//    public JedisConnectionFactory connectionFactory() {
//        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
//        configuration.setHostName(redisHost);
//        configuration.setPort(redisPort);
//        return new JedisConnectionFactory(configuration);
//    }
//
//    @Bean
//    public RedisTemplate<String, Object> template() {
//        RedisTemplate<String, Object> template = new RedisTemplate<>();
//        template.setConnectionFactory(connectionFactory());
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setHashKeySerializer(new StringRedisSerializer());
//        template.setHashKeySerializer(new JdkSerializationRedisSerializer());
//        template.setValueSerializer(new JdkSerializationRedisSerializer());
//        template.setEnableTransactionSupport(true);
//        template.afterPropertiesSet();
//        return template;
//    }

}

