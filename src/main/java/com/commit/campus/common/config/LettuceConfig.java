package com.commit.campus.common.config;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;

@Configuration
@EnableRedisRepositories
public class LettuceConfig {

    @Value("${spring.data.redis.port}")
    public int port;

    @Value("${spring.data.redis.host}")
    public String host;

    @Value("${spring.data.redis.password}")
    private String password;

    @Bean
    public RedisClient redisClient() {
        // Redis 서버 URL 설정
//        return RedisClient.create("redis://localhost:"+ port);
        return RedisClient.create("redis://:" + password + "@" + host + ":" + port); // TODO: EC2에 설치된 레디스 연결 시 변경
    }

    @Bean
    public StatefulRedisConnection<String, String> redisConnection(RedisClient redisClient) {
        // Redis 연결 생성
        return redisClient.connect();
    }

    @Bean
    // stateful vs stateless 차이 정리
    // https://velog.io/@adc0612/stateful%EA%B3%BC-stateless-%EC%B0%A8%EC%9D%B4-7tfkp7a4
    public RedisAsyncCommands<String, String> redisAsyncCommands(StatefulRedisConnection<String, String> connection) {
        // 비동기 명령어 객체 생성
        return connection.async();      // 동기인 경우 connection.sync();
    }

    @Bean
    public RedisCommands<String, String> redisSyncCommands(StatefulRedisConnection<String, String> connection) {
        // 동기 명령어 객체 생성
        return connection.sync();
    }
}
