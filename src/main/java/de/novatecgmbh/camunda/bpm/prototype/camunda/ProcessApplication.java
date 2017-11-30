package de.novatecgmbh.camunda.bpm.prototype.camunda;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import javax.sql.DataSource;

@SpringBootApplication(scanBasePackages = "de.novatecgmbh.camunda.bpm.prototype")
@EnableProcessApplication
public class ProcessApplication {
	public static void main(final String... args) throws Exception {
	    SpringApplication.run(ProcessApplication.class, args);
  	}
}
