package com.example.project_three_event_management_system;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "Spring Boot REST API Documentation",
				description = "Spring Boot REST API's endpoint used to take request as per requirements to connect database collect data's to response to user",
				version = "v1.0",
				contact = @Contact(
						name = "James",
						email = "james.projects@gamil.com"
				)
		)
)

public class ProjectThreeEventManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectThreeEventManagementSystemApplication.class, args);
	}

}
