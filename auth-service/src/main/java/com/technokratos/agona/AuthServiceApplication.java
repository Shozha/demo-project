package com.technokratos.agona;

import com.technokratos.agona.properties.JwtProperties;
import com.technokratos.agona.properties.RefreshTokenProperties;
import com.technokratos.agona.properties.SecurityConfigProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigurationProperties({
		JwtProperties.class,
		RefreshTokenProperties.class,
		SecurityConfigProperties.class
})
public class AuthServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthServiceApplication.class, args);
	}

}
