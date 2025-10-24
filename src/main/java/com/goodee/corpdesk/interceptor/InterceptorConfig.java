package com.goodee.corpdesk.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Autowired
    private UserProfileInterceptor userProfileInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(userProfileInterceptor)
            .addPathPatterns(
//                "/admin/**",
//                "/approval/**",
//                "/approval-form/**",
//                "/attendance/**",
//                "/board/**",
//                "/calendar/**",
//                "/chat/**",
//                "/email/**",
//                "/employee/**",
//                "/dashboard/**",
//                "/notification/**",
//                "/organization/**",
//                "/position/**",
//                "/salary/**",
//                "/personal-schedule/**",
//                "/stats/**",
//                "/vacation/**"
                "/**"
            );
    }
}