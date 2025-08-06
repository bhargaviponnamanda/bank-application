package com.project.Bank_Application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;

@SpringBootApplication(scanBasePackages = "com.project")
@EnableJpaRepositories(basePackages = "com.project.repository")
@EntityScan(basePackages = "com.project.entity")
@OpenAPIDefinition(
		         info = @Info(
		        		 title = "Bank Application",
		                 description = "Backend Rest APIs for Bank App",
		                 version = "v1.0",
		                 contact = @Contact(
		                		 name = "Bhargavi",
		                		 email = "bhargaviponnamanda1999@gmail.com",
		                		 url = "https://github.com/bhargaviponnamanda/bank-application"
		                ),
		                 license = @License(
		                		 name = "Bhargavi",
		                		 url = "https://github.com/bhargaviponnamanda/bank-application"
		                  )
		        		),
		           externalDocs = @ExternalDocumentation(
		        		   description = "Bank Application",
		        		   url = "https://github.com/bhargaviponnamanda/bank-application"
		        		   )
		         )

public class BankApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankApplication.class, args);
	}
		
}
