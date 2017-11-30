package de.novatecgmbh.camunda.bpm.prototype.camunda;

import org.camunda.bpm.spring.boot.starter.annotation.EnableProcessApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(scanBasePackages = "de.novatecgmbh.camunda.bpm.prototype")
@EnableProcessApplication
public class ProcessApplication {
	public static void main(final String... args) throws Exception {
	    SpringApplication.run(ProcessApplication.class, args);
  	}
}
