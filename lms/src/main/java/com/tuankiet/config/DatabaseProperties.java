package com.tuankiet.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

/**
 * Database configuration properties
 * 
 * @author tuankiet
 * @version 1.0.0
 * @since 1.0.0
 */
@Component
@PropertySource("classpath:database.properties")
public class DatabaseProperties {

    @Value("${db.driver}")
    private String driverClassName;

    @Value("${db.url}")
    private String url;

    @Value("${db.username}")
    private String username;

    @Value("${db.password}")
    private String password;

    @Value("${hibernate.dialect}")
    private String hibernateDialect;

    @Value("${hibernate.hbm2ddl.auto}")
    private String hibernateHbm2ddlAuto;

    @Value("${hibernate.show_sql}")
    private String hibernateShowSql;

    @Value("${hibernate.format_sql}")
    private String hibernateFormatSql;

    @Value("${hibernate.use_sql_comments}")
    private String hibernateUseSqlComments;

    @Value("${hibernate.cache.use_second_level_cache}")
    private String hibernateCacheUseSecondLevelCache;

    @Value("${hibernate.cache.region.factory_class}")
    private String hibernateCacheRegionFactoryClass;

    @Value("${hibernate.cache.use_query_cache}")
    private String hibernateCacheUseQueryCache;

    // Getters
    public String getDriverClassName() {
        return driverClassName;
    }

    public String getUrl() {
        return url;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getHibernateDialect() {
        return hibernateDialect;
    }

    public String getHibernateHbm2ddlAuto() {
        return hibernateHbm2ddlAuto;
    }

    public String getHibernateShowSql() {
        return hibernateShowSql;
    }

    public String getHibernateFormatSql() {
        return hibernateFormatSql;
    }

    public String getHibernateUseSqlComments() {
        return hibernateUseSqlComments;
    }

    public String getHibernateCacheUseSecondLevelCache() {
        return hibernateCacheUseSecondLevelCache;
    }

    public String getHibernateCacheRegionFactoryClass() {
        return hibernateCacheRegionFactoryClass;
    }

    public String getHibernateCacheUseQueryCache() {
        return hibernateCacheUseQueryCache;
    }
}
