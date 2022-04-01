package com.marti.humanresbackend.configurations;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(title = "HumanResBackend", version = "0.0.1", description = "Documentation APIs v0.0.1"))
public class AppConfig {

}
