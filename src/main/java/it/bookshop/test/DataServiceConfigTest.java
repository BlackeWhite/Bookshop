package it.bookshop.test;

import java.util.Properties;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import it.bookshop.app.DataServiceConfig;

@Configuration
@ComponentScan(basePackages = {"it.bookshop.model"})
@EnableTransactionManagement
public class DataServiceConfigTest extends DataServiceConfig {

	@Bean
	@Override
	protected Properties hibernateProperties() {
		Properties hibernateProp = super.hibernateProperties();
		hibernateProp.put("javax.persistence.schema-generation.database.action", "update");
		return hibernateProp;
	}
}
