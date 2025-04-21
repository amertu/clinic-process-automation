package io.camunda.server.clinic;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan("io.camunda.server.clinic.dto")
public class ClinicCamundaServerApplication {

	public static void main(String... args) {
		SpringApplication.run(ClinicCamundaServerApplication.class, args);
	}

}