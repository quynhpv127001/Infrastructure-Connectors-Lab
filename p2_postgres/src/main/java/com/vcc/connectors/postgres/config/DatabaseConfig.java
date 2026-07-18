package com.vcc.connectors.postgres.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig {

    // ==========================================
    // CẤU HÌNH TENANT A (DB 1)
    // ==========================================
    @Value("${custom.datasource.db1.url}")
    private String db1Url;

    @Value("${custom.datasource.db1.username}")
    private String db1Username;

    @Value("${custom.datasource.db1.password}")
    private String db1Password;

    @Value("${custom.datasource.db1.driver-class-name}")
    private String db1DriverClassName;

    @Value("${custom.datasource.db1.pool-name}")
    private String db1PoolName;

    @Value("${custom.datasource.db1.maximum-pool-size}")
    private int db1MaximumPoolSize;

    @Bean(name = "dataSourceDb1")
    @Primary
    public DataSource dataSourceDb1() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(db1Url);
        config.setUsername(db1Username);
        config.setPassword(db1Password);
        config.setDriverClassName(db1DriverClassName);
        config.setPoolName(db1PoolName);
        config.setMaximumPoolSize(db1MaximumPoolSize);
        config.setConnectionTimeout(2000);
        config.setInitializationFailTimeout(-1);
        return new HikariDataSource(config);
    }

    @Bean(name = "jdbcTemplateDb1")
    @Primary
    public JdbcTemplate jdbcTemplateDb1() {
        return new JdbcTemplate(dataSourceDb1());
    }

    @Bean(name = "transactionManagerDb1")
    @Primary
    public PlatformTransactionManager transactionManagerDb1() {
        return new DataSourceTransactionManager(dataSourceDb1());
    }

    // ==========================================
    // CẤU HÌNH TENANT B (DB 2)
    // ==========================================
    @Value("${custom.datasource.db2.url}")
    private String db2Url;

    @Value("${custom.datasource.db2.username}")
    private String db2Username;

    @Value("${custom.datasource.db2.password}")
    private String db2Password;

    @Value("${custom.datasource.db2.driver-class-name}")
    private String db2DriverClassName;

    @Value("${custom.datasource.db2.pool-name}")
    private String db2PoolName;

    @Value("${custom.datasource.db2.maximum-pool-size}")
    private int db2MaximumPoolSize;

    @Bean(name = "dataSourceDb2")
    public DataSource dataSourceDb2() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(db2Url);
        config.setUsername(db2Username);
        config.setPassword(db2Password);
        config.setDriverClassName(db2DriverClassName);
        config.setPoolName(db2PoolName);
        config.setMaximumPoolSize(db2MaximumPoolSize);
        config.setConnectionTimeout(2000);
        config.setInitializationFailTimeout(-1);
        return new HikariDataSource(config);
    }

    @Bean(name = "jdbcTemplateDb2")
    public JdbcTemplate jdbcTemplateDb2() {
        return new JdbcTemplate(dataSourceDb2());
    }

    @Bean(name = "transactionManagerDb2")
    public PlatformTransactionManager transactionManagerDb2() {
        return new DataSourceTransactionManager(dataSourceDb2());
    }
}
