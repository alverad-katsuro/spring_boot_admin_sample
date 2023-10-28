package br.com.alverad.anexos_mongo.config.swagger;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;

/**
 * Responsável pelas configurações do Swagger.
 *
 * @author Alfredo Gabriel
 * @since 23/01/2023
 * @version 1.0
 */
@Configuration
public class SwaggerConfig {
	@Bean
	public OpenAPI springShopOpenAPI() {
		return new OpenAPI()
				.components(new Components()
						.addSecuritySchemes("Bearer",
								new SecurityScheme().type(SecurityScheme.Type.HTTP)
										.scheme("bearer").bearerFormat("JWT")))
				.info(new Info()
						.title("Anexos API")
						.description(
								"API de anexos desenvolvida por Alfredo Gabriel.")
						.version("v0.0.1")
						.license(new License()
								.name("Ainda a decidir")
								.url("adsadas")))
				.externalDocs(new ExternalDocumentation()
						.description("Wiki do Projeto no Gitlab")
						.url("asdasdsad"));
	}
}
