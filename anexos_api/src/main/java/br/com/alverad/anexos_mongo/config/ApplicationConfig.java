package br.com.alverad.anexos_mongo.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

import br.com.alverad.anexos_mongo.config.properties.CorsProperties;

@Configuration
@EnableScheduling
@EnableDiscoveryClient
@EnableConfigurationProperties(value = { CorsProperties.class })
public class ApplicationConfig {

}