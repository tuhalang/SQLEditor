package com.tuhalang.sqleditor.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.util.HashMap;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = {"com.tuhalang.sqleditor.repo"}, entityManagerFactoryRef = "entityManagerFactory", transactionManagerRef = "transactionManager")
public class DatasourceConfig {
    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean({"datasource"})
    public DataSource datasourceV1() {
        return DataSourceBuilder.create().build();
    }

    @Bean({"entityManagerFactory"})
    public LocalContainerEntityManagerFactoryBean bccs1EntityManager(@Qualifier("datasource") DataSource datasource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        em.setDataSource(datasource);
        em.setPersistenceUnitName("news");
        em.setPackagesToScan(new String[] { "com.tuhalang.sqleditor.domain" });
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        em.setJpaVendorAdapter(vendorAdapter);
        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.dialect", "org.hibernate.dialect.Oracle12cDialect");
        properties.put("hibernate.enable_lazy_load_no_trans", "true");
        em.setJpaPropertyMap(properties);
        return em;
    }

    @Bean({"transactionManager"})
    public JpaTransactionManager transactionManagerV1(@Qualifier("entityManagerFactory") LocalContainerEntityManagerFactoryBean entityManagerV1) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerV1.getObject());
        return transactionManager;
    }

    @Bean({"transactionTemplate"})
    public TransactionTemplate transactionTemplate(@Qualifier("transactionManager") JpaTransactionManager transactionManager) {
        return new TransactionTemplate(transactionManager);
    }

    @Bean({"jdbcTemplate"})
    public JdbcTemplate jdbcTemplate(@Qualifier("datasource") DataSource datasource) {
        return new JdbcTemplate(datasource);
    }
}
