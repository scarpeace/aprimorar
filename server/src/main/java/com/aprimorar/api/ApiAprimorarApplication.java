package com.aprimorar.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.modulith.Modulithic;

@Modulithic(
		additionalPackages = "com.aprimorar.api.domain",
		sharedModules = {"config", "shared", "enums", "exception"}
)
@SpringBootApplication
public class ApiAprimorarApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiAprimorarApplication.class, args);
	}

}
