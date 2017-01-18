package ee;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.jooq.SQLDialect;
import org.jooq.impl.DataSourceConnectionProvider;
import org.jooq.impl.DefaultConfiguration;
import org.jooq.impl.DefaultDSLContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.bind.RelaxedPropertyResolver;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.jdbc.datasource.TransactionAwareDataSourceProxy;

import javax.sql.DataSource;

@Configuration
public class DatabaseConfig implements EnvironmentAware {

  private static final Logger LOG = LoggerFactory.getLogger(DatabaseConfig.class);

  private RelaxedPropertyResolver propertyResolver;
  private Environment env;


  @Override
  public void setEnvironment(Environment env) {
    this.env = env;
    this.propertyResolver = new RelaxedPropertyResolver(env, "spring.datasource.");
  }

  @Bean(destroyMethod = "shutdown")
  @Qualifier("dataSource")
  @Primary
  public DataSource dataSource() {
    LOG.info("Configuring Datasource, url: {}, user: {}", propertyResolver.getProperty("url"), propertyResolver.getProperty("username"));
    HikariConfig config = new HikariConfig();
    config.setPoolName("orderapi");
    config.setDataSourceClassName(propertyResolver.getProperty("dataSourceClassName"));
    config.addDataSourceProperty("url", propertyResolver.getProperty("url"));
//    config.addDataSourceProperty("user", propertyResolver.getProperty("username"));
//    config.addDataSourceProperty("password", propertyResolver.getProperty("password"));
    config.setConnectionTimeout(3000);
    config.setConnectionTestQuery("SELECT 1");

    return getHikariDataSource(config);
  }

  HikariDataSource getHikariDataSource(HikariConfig config) {
    return new HikariDataSource(config);
  }



  @Bean
  @Primary
  public DataSourceTransactionManager transactionManager() {
    return new DataSourceTransactionManager(dataSource());
  }

  @Bean
  public DataSourceConnectionProvider dataSourceConnectionProvider() {
    return new DataSourceConnectionProvider(new TransactionAwareDataSourceProxy(dataSource()));
  }

  @Bean
  public DefaultDSLContext dsl() {
    return new DefaultDSLContext(jooqConfig());
  }

//  @Bean
//  public JOOQToSpringExceptionTransformer exceptionTransformer() {
//    return new JOOQToSpringExceptionTransformer();
//  }

  @Bean
  public DefaultConfiguration jooqConfig() {
    DefaultConfiguration defaultConfiguration = new DefaultConfiguration();

    defaultConfiguration.set(dataSourceConnectionProvider());
//    defaultConfiguration.set(new DefaultExecuteListenerProvider(
//      exceptionTransformer()
//    ));
    defaultConfiguration.set(SQLDialect.H2);
    return defaultConfiguration;
  }

}

