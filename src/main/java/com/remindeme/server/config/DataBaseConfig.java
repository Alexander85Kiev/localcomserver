package com.remindeme.server.config;


import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

@Configuration
@EnableJpaRepositories("com.remindeme.server.repository")
@EnableTransactionManagement
@ComponentScan("com.remindeme.server.repository")
@PropertySource("classpath:db.properties")
public class DataBaseConfig {


    @Resource
    private Environment env;

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean() {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(dataSource());
        em.setPackagesToScan(env.getRequiredProperty("db.entity.package"));

        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        em.setJpaProperties(getHibernateProperties());
        return em;
    }

    public Properties getHibernateProperties() {
        try {
            Properties properties = new Properties();
            InputStream is = getClass().getClassLoader().getResourceAsStream("hibernate.Properties");
            properties.load(is);
            return properties;
        } catch (IOException e) {
            throw new IllegalArgumentException("Can't find 'hibernat.propertise' in classpath!",e);
        }


    }

    @Bean
    public DataSource dataSource() {
        BasicDataSource ds = new BasicDataSource();
        ds.setUrl(env.getRequiredProperty("db.url"));
        ds.setDriverClassName(env.getProperty("db.driver"));
        ds.setUsername(env.getProperty("db.username"));
        ds.setPassword(env.getProperty("db.password"));

        ds.setInitialSize(Integer.valueOf(env.getRequiredProperty("db.initialSize")));
        ds.setMinIdle(Integer.valueOf(env.getRequiredProperty("db.minIdle")));
        ds.setTimeBetweenEvictionRunsMillis(Long.valueOf(env.getRequiredProperty("db.timeBetweenEvictionRunsMillis")));
        ds.setMaxIdle(Integer.valueOf(env.getRequiredProperty("db.maxidLe")));
        ds.setMinEvictableIdleTimeMillis(Long.valueOf(env.getRequiredProperty("db.minEvictableIdleTimeMillis")));
        ds.setTestOnBorrow(Boolean.valueOf(env.getRequiredProperty("db.testOnBorrow")));
        ds.setValidationQuery(env.getRequiredProperty("db.validationQuery"));



        return ds;
    }

    @Bean
    public PlatformTransactionManager platformTransactionManager(){
        JpaTransactionManager manager = new JpaTransactionManager();
        manager.setEntityManagerFactory(entityManagerFactoryBean().getObject());
        return manager;
    }


}
