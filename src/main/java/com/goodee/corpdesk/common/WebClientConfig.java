package com.goodee.corpdesk.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class WebClientConfig {
    /**
     * URI 인코딩을 자동으로 수행하지 않도록 구성된 WebClient를 생성하여 반환합니다.
     *
     * 반환되는 인스턴스는 EncodingMode.NONE으로 설정된 DefaultUriBuilderFactory를 사용하므로,
     * 제공된 URI는 빌더에 의해 그대로 유지됩니다.
     *
     * @return 인코딩이 비활성화된 DefaultUriBuilderFactory를 사용하는 WebClient 인스턴스
     */
    @Bean
    public WebClient webClient(){

        DefaultUriBuilderFactory factory = new DefaultUriBuilderFactory();
        factory.setEncodingMode(DefaultUriBuilderFactory.EncodingMode.NONE);

        return WebClient.builder()
            .uriBuilderFactory(factory)
            .build();
    }
}
