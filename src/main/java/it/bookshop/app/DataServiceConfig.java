package it.bookshop.app;

import java.io.IOException;
import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import it.bookshop.test.DataServiceConfigTest;


@Configuration
@ComponentScan(basePackages = { "it.bookshop.model" },
excludeFilters  = {@ComponentScan.Filter(
		type = FilterType.ASSIGNABLE_TYPE, classes = {DataServiceConfigTest.class})})

@EnableTransactionManagement
@PropertySource("classpath:dbconfig.properties")
public class DataServiceConfig {

	@Value("${db.host}")
	private String db_host;
	@Value("${db.port}")
	private String db_port;
	@Value("${db.name}")
	private String db_name;
	@Value("${db.username}")
	private String db_usr;
	@Value("${db.password}")
	private String db_pw;
	private static Logger logger = LoggerFactory.getLogger(DataServiceConfig.class);

	@Bean
	public DataSource dataSource() {
		try {

			DriverManagerDataSource ds = new DriverManagerDataSource();
			ds.setDriverClassName(com.mysql.jdbc.Driver.class.getName());
			ds.setUrl("jdbc:mysql://" + db_host + ":" + db_port + "/" + db_name + "?createDatabaseIfNotExist=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC&useSSL=false");
			ds.setUsername(db_usr);
			ds.setPassword(db_pw);
			return ds;
		
		} catch (Exception e) {
			logger.error("DataSource bean cannot be created!!", e);
			

			return null;
			
		}
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}

	@Bean
	protected Properties hibernateProperties() {
		Properties hibernateProp = new Properties();
		hibernateProp.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
		hibernateProp.put("hibernate.format_sql", true);
		hibernateProp.put("hibernate.use_sql_comments", true);
		hibernateProp.put("hibernate.show_sql", true);
		hibernateProp.put("hibernate.max_fetch_depth", 3);
		hibernateProp.put("hibernate.jdbc.batch_size", 10);
		hibernateProp.put("hibernate.jdbc.fetch_size", 50);
		hibernateProp.put("javax.persistence.schema-generation.database.action", "none"); // importante, altrimenti si
																							// aspetta il DB gia`
																							// "strutturato"
		return hibernateProp;
	}

	@Bean
	@Autowired
	public SessionFactory sessionFactory(DataSource dataSource, Properties hibernateProperties) throws IOException {
		LocalSessionFactoryBean sessionFactoryBean = new LocalSessionFactoryBean();
		sessionFactoryBean = new LocalSessionFactoryBean();
		sessionFactoryBean.setDataSource(dataSource);
		sessionFactoryBean.setPackagesToScan("it.bookshop.model");
		sessionFactoryBean.setHibernateProperties(hibernateProperties);
		sessionFactoryBean.afterPropertiesSet();
		return sessionFactoryBean.getObject();
	}

	
	@Bean
	@Autowired
	public PlatformTransactionManager transactionManager(SessionFactory sessionFactory) throws IOException {
		return new HibernateTransactionManager(sessionFactory);
	}
}
