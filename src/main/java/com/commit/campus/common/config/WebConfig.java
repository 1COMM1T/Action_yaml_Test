package com.commit.campus.common.config;

import com.commit.campus.common.CustomHandlerMethodArgumentResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {


    private CustomHandlerMethodArgumentResolver customHandlerMethodArgumentResolver;

    @Autowired
    public WebConfig(CustomHandlerMethodArgumentResolver customHandlerMethodArgumentResolver) {
        this.customHandlerMethodArgumentResolver = customHandlerMethodArgumentResolver;
    }

    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(customHandlerMethodArgumentResolver);
    }
}
