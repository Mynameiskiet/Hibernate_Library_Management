package com.tuankiet.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * Main application configuration
 * 
 * @author tuankiet
 * @version 1.0.0
 * @since 1.0.0
 */
@Configuration
@ComponentScan(basePackages = {
    "com.tuankiet.cli",
    "com.tuankiet.services",
    "com.tuankiet.repositories", 
    "com.tuankiet.utils",
    "com.tuankiet.exceptions",
    "com.tuankiet.config",
    "com.tuankiet.validators" // Added validators package to component scan
})
@Import({
    DataSourceConfig.class,
    HibernateConfig.class,
    ValidationConfig.class,
    DatabaseProperties.class // Explicitly import DatabaseProperties
})
public class ApplicationConfig {
    
    // Added PropertySourcesPlaceholderConfigurer bean for property resolution
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
