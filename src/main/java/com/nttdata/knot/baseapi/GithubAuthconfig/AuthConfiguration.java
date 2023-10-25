package com.nttdata.knot.baseapi.GithubAuthconfig;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
@EnableMethodSecurity
public class AuthConfiguration {

        private final IAuthenticationService authenticationService;

        @Autowired
        public AuthConfiguration(IAuthenticationService authenticationService) {
                this.authenticationService = authenticationService;
        }

        @Bean
        public WebMvcConfigurer corsConfigurer() {
                return new WebMvcConfigurer() {
                        @Override
                        public void addCorsMappings(CorsRegistry registry) {
                                registry.addMapping("/**")
                                                .allowedOrigins("*")
                                                .allowedMethods("*")
                                                .allowedHeaders("*");
                        }
                };
        }

        // To use in local
        // @Bean
        // public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //         http.cors().and().authorizeHttpRequests(authorize -> authorize
        //                         .requestMatchers("*/api-docs/**").permitAll()
        //                         .requestMatchers("/swagger-ui/**").permitAll()
        //                         .anyRequest().authenticated()

        //         )

        //                         .oauth2ResourceServer(oauth2 -> oauth2
        //                                         .opaqueToken(opaqueToken -> opaqueToken
        //                                                         .introspector(introspector())));
        //         return http.build();
        // }

        // To use in cluster
        @Bean
        public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

                http.authorizeHttpRequests(authorize -> authorize
                                .requestMatchers("/api-docs/**").permitAll()
                                .requestMatchers("/swagger-ui/**").permitAll()
                                .anyRequest().authenticated()

                )

                                .oauth2ResourceServer(oauth2 -> oauth2
                                                .opaqueToken(opaqueToken -> opaqueToken
                                                                .introspector(introspector())));
                return http.build();
        }

        @Bean
        public OpaqueTokenIntrospector introspector() {
                return new GithubTokenIntrospector(authenticationService);
        }

        @Bean
        public OpenAPI customOpenAPI() {
                final String securitySchemeName = "githubToken";
                return new OpenAPI()
                                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                                .components(
                                                new Components()
                                                                .addSecuritySchemes(securitySchemeName,
                                                                                new SecurityScheme()
                                                                                                .name(securitySchemeName)
                                                                                                .type(SecurityScheme.Type.APIKEY)
                                                                                                .in(SecurityScheme.In.HEADER)
                                                                                                .name("Authorization")));
        }

}