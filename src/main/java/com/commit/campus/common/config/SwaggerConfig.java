package com.commit.campus.common.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {
    @Bean
    public OpenAPI swaggerOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Campus Swagger API 명세서") // API 문서의 제목 설정
                        .version("1.0") // API 버전 설정
                        .description("Camping 애플리케이션의 API 문서입니다. 이 API는 AWS Lambda를 통해 고캠핑 API 데이터를 통합하여 제공합니다.") // API 설명 설정
                );
    }
}