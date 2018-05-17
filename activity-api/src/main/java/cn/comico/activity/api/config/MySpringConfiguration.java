package cn.comico.activity.api.config;

import java.util.Properties;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.repository.query.QueryLookupStrategy.Key;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.DatabasePopulatorUtils;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;


/**
 * 要使用ioc 和 jpa 必须有这个配置类
 */
@Configuration
@PropertySource(value = { "classpath:application.properties" })
@EnableJpaRepositories(basePackages = {"cn.comico.activity.dao"})
@ComponentScan("cn.comico.activity")
public class MySpringConfiguration{

	@Autowired	//默认按类型
	private Environment env;

	/**
	 * dataSource
	 * 
	 */
	@Bean
	@Autowired
	public DataSource dataSource(DatabasePopulator databasePopulator) {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		dataSource.setDriverClassName(env.getProperty("jdbc.driverClass"));
		dataSource.setUrl(env.getProperty("jdbc.url"));
		dataSource.setUsername(env.getProperty("jdbc.username"));
		dataSource.setPassword(env.getProperty("jdbc.password"));
		DatabasePopulatorUtils.execute(databasePopulator, dataSource);
		return dataSource;
	}
	
	
	/**
	 * entityManager
	 */
	@Bean
	@Autowired
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		
		HibernateJpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
		jpaVendorAdapter.setGenerateDdl(true);
		
		Properties properties = new Properties();
		properties.setProperty("hibernate.dialect", env.getProperty("hibernate.dialect"));
		properties.setProperty("hibernate.show_sql", env.getProperty("hibernate.show_sql"));
		
		factory.setDataSource(dataSource);
		factory.setJpaProperties(properties);
		factory.setPackagesToScan(env.getProperty("pojo.scan.path"));
		factory.setJpaVendorAdapter(jpaVendorAdapter);
		
		return factory;
	}
	
	/**
	 * transactionManager
	 */
	@Bean
	@Autowired
	public PlatformTransactionManager transactionManager(LocalContainerEntityManagerFactoryBean factory) {
		return new JpaTransactionManager(factory.getObject());
	}
	
	/**
	 * 数据库初始化化或清理工具类或设置类
	 * @return
	 */
	@Bean
	@Autowired
	public DatabasePopulator  databasePopulator() {
		ResourceDatabasePopulator databasePopulartor = new ResourceDatabasePopulator();
		databasePopulartor.setContinueOnError(false);
//		databasePopulartor.addScript(new ClassPathResource("dept.sql"));
		return databasePopulartor;
	}
}
