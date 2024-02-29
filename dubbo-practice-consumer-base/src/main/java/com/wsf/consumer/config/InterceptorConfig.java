package com.wsf.consumer.config;

import com.wsf.consumer.interceptor.HttpTraceInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport {

    @Bean
    public HttpTraceInterceptor httpTraceInterceptor() {
        return new HttpTraceInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(httpTraceInterceptor()).addPathPatterns("/**");
        super.addInterceptors(registry);
    }

}
