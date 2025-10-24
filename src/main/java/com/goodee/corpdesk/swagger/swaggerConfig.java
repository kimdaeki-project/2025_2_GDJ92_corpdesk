package com.goodee.corpdesk.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class swaggerConfig  {
	 @Bean
	    public OpenAPI customOpenAPI() {
	        return new OpenAPI()
	                .info(new Info().title("Open API 문서")
	                        .description("그룹웨어 서비스 API")
	                        .version("1.0"))
	                .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
	                .components(new Components()
	                        .addSecuritySchemes("bearerAuth", new SecurityScheme()
	                        .type(SecurityScheme.Type.HTTP)
	                        .scheme("bearer")
	                        .bearerFormat("JWT")
	                        .description("Bearer + JWT 토큰을 입력하세요")));
	                
	    }
}
