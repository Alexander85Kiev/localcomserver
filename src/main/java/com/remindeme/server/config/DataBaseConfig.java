package com.remindeme.server.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("com.remindeme.server.repository")
public class DataBaseConfig {
}
