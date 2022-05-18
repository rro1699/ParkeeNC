package com.example.ncproject.config;

import com.example.ncproject.Services.ServiceUtils.TokenVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration

public class AppConfig {
    @Value("${CLIENT_ID}")
    private String CLIENT_ID;

    @Bean
    public TokenVerifier getTokenVerifier(){
        TokenVerifier tokenVerifier = new TokenVerifier(CLIENT_ID);
        tokenVerifier.Init();
        return tokenVerifier;
    }
}
