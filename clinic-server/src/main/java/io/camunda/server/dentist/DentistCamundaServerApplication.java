package io.camunda.server.dentist;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("io.camunda.server.dentist.dto")
public class DentistCamundaServerApplication {

	public static void main(String... args) {
		SpringApplication.run(DentistCamundaServerApplication.class, args);
	}

}