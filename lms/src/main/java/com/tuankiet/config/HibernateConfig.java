package com.tuankiet.config;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * Hibernate configuration
 * 
 * @author tuankiet
 * @version 1.0.0
 * @since 1.0.0
 */
@Configuration
public class HibernateConfig {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private DatabaseProperties databaseProperties;

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);
        sessionFactory.setPackagesToScan("com.tuankiet.entities");
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
    }

    @Bean
    public PlatformTransactionManager hibernateTransactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactory().getObject());
        return transactionManager;
    }

    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", databaseProperties.getHibernateDialect());
        properties.put("hibernate.hbm2ddl.auto", databaseProperties.getHibernateHbm2ddlAuto());
        properties.put("hibernate.show_sql", databaseProperties.getHibernateShowSql());
        properties.put("hibernate.format_sql", databaseProperties.getHibernateFormatSql());
        properties.put("hibernate.use_sql_comments", databaseProperties.getHibernateUseSqlComments());
        
        // Temporarily disable second level cache to avoid configuration issues
        properties.put("hibernate.cache.use_second_level_cache", "false");
        properties.put("hibernate.cache.use_query_cache", "false");
        
        // Performance optimizations
        properties.put("hibernate.jdbc.batch_size", "20");
        properties.put("hibernate.order_inserts", "true");
        properties.put("hibernate.order_updates", "true");
        properties.put("hibernate.jdbc.batch_versioned_data", "true");
        properties.put("hibernate.temp.use_jdbc_metadata_defaults", "false");
        
        return properties;
    }
}
