package com.cursosrecomendados.telegram.configuration;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
@EnableJpaRepositories("packages.to.scan")
public class JpaConfiguration {
    @Value("${spring.datasource.driver-class-name}")
	String springDriver;
	
	@Value("${spring.datasource.url}")
	String springUrl;
	
	@Value("${spring.datasource.username}")
	String springName;
	
	@Value("${spring.datasource.password}")
	String springPass;
	
	
    @Bean
    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(springDriver);
        dataSource.setUrl(springUrl);
        dataSource.setUsername(springName);
        dataSource.setPassword(springPass);
        return dataSource;
    }

    @Bean
    public JpaTransactionManager transactionManager(EntityManagerFactory emf) {
      return new JpaTransactionManager(emf);
    }

    @Bean
    public JpaVendorAdapter jpaVendorAdapter() {
      HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
      jpaVendorAdapter.setDatabase(Database.MYSQL);
      jpaVendorAdapter.setGenerateDdl(true);
      return jpaVendorAdapter;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
      LocalContainerEntityManagerFactoryBean lemfb = new LocalContainerEntityManagerFactoryBean();
      lemfb.setDataSource(dataSource());
      lemfb.setJpaVendorAdapter(jpaVendorAdapter());
      lemfb.setPackagesToScan("packages.containing.entity.classes");
      return lemfb;
    }
}