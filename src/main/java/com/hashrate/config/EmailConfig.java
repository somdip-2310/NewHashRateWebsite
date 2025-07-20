package com.hashrate.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class EmailConfig {

    /**
     * Mock JavaMailSender for local development when email is disabled
     */
    @Bean
    @Primary
    @ConditionalOnProperty(name = "app.email.enabled", havingValue = "false", matchIfMissing = true)
    public JavaMailSender mockJavaMailSender() {
        return new JavaMailSenderImpl(); // Creates a basic implementation that won't actually send emails
    }
}