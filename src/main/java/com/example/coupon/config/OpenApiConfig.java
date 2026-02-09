package com.example.coupon.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI couponApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("Coupon API")
                        .description("Technical Challenge - Coupon Management")
                        .version("1.0.0"));
    }
}
