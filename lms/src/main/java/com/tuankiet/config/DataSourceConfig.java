package com.tuankiet.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * DataSource configuration for the Library Management System.
 * 
 * @author tuankiet
 * @version 1.0.0
 * @since 1.0.0
 */
@Configuration
public class DataSourceConfig {
    
    @Autowired
    private DatabaseProperties databaseProperties;
    
    @Bean
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        
        // Basic connection properties
        config.setDriverClassName(databaseProperties.getDriverClassName());
        config.setJdbcUrl(databaseProperties.getUrl());
        config.setUsername(databaseProperties.getUsername());
        config.setPassword(databaseProperties.getPassword());
        
        // Pool configuration
        config.setMinimumIdle(5);
        config.setMaximumPoolSize(20);
        config.setConnectionTimeout(30000); // 30 seconds
        config.setIdleTimeout(600000); // 10 minutes
        config.setMaxLifetime(1800000); // 30 minutes
        config.setLeakDetectionThreshold(60000); // 1 minute
        
        // Connection validation
        config.setConnectionTestQuery("SELECT 1");
        
        // Pool name for monitoring
        config.setPoolName("LibraryManagementPool");
        
        return new HikariDataSource(config);
    }
}
