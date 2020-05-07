package ua.vlasov_eugene.delivery_service.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.sql2o.Sql2o;

import javax.sql.DataSource;

@Configuration
public class Sql2OConfig {
	@Value("${datasource}")
	private String jdbcUrl;

	@Bean
	public DataSource dataSource() {
		HikariConfig config = new HikariConfig();
		config.setJdbcUrl(jdbcUrl);
		config.setDriverClassName("org.postgresql.Driver");
		config.setMinimumIdle(10);
		config.setMaximumPoolSize(100);
		config.setConnectionInitSql("SELECT 1");
		return new HikariDataSource(config);
	}

	@Bean
	public Sql2o sql2o() {
		return new Sql2o(dataSource());
	}
}
