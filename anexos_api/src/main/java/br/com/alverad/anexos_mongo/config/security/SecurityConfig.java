package br.com.alverad.anexos_mongo.config.security;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import br.com.alverad.anexos_mongo.config.properties.CorsProperties;
import lombok.RequiredArgsConstructor;

/**
 * Responsável pelas configurações de segurança da aplicação.
 *
 * @author Alfredo Gabriel
 * @since 23/01/2023
 * @version 1.0
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

	private final CorsProperties corsProperties;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

		httpSecurity.csrf(AbstractHttpConfigurer::disable)
				.cors(cors -> cors.configurationSource(corsConfigurationSource()))
				.anonymous(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(authorize -> authorize
						.requestMatchers(
								"/v3/api-docs/**",
								"/swagger-ui/**",
								"/swagger-ui.html",
								"/actuator",
								"/actuator/health",
								"/")
						.permitAll()
						.requestMatchers("/actuator/**").hasRole("SPRING_ACTUATOR")
						.anyRequest().authenticated())
				.oauth2ResourceServer(oauth2 -> oauth2
						.jwt(Customizer.withDefaults()))
				.sessionManagement(session -> session
						.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.exceptionHandling(exeption -> exeption
						.authenticationEntryPoint(
								new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED)));

		return httpSecurity.build();
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		configuration.applyPermitDefaultValues();
		configuration.setAllowedOriginPatterns(List.of(corsProperties.getAllowedOriginPatterns()));
		configuration.setAllowedHeaders(Arrays.asList("Authorization", "Requestor-Type", "Content-Type",
				"Access-Control-Allow-Headers", "access-control-allow-origin"));
		configuration.setExposedHeaders(Arrays.asList("X-Get-Header", "Access-Control-Allow-Methods"));
		configuration.setAllowedMethods(
				Arrays.asList("POST", "GET", "PUT", "DELETE", "PATCH"));
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}

	@Bean
	@SuppressWarnings("unchecked")
	public JwtAuthenticationConverter jwtAuthenticationConverter() {
		JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
		jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(
				jwt -> {
					Object roles = jwt.getClaimAsMap("realm_access").get("roles");
					if (roles instanceof List<?>) {
						List<String> rolesList = (List<String>) roles;
						JwtGrantedAuthoritiesConverter scopesConverter = new JwtGrantedAuthoritiesConverter();
						Collection<GrantedAuthority> allAuthorities = scopesConverter
								.convert(jwt);
						allAuthorities.addAll(
								rolesList.stream()
										.map(role -> new SimpleGrantedAuthority(
												"ROLE_" + role))
										.toList());
						return allAuthorities;
					} else {
						return Collections.emptyList();
					}
				});

		return jwtAuthenticationConverter;
	}
}