package com.ucb.plataforma.res.reviews.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverterAdapter;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;

import java.util.*;
import java.util.stream.Collectors;
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .authorizeExchange(auth -> auth
                // públicos
                .pathMatchers(
                    "/actuator/**",
                    "/eureka/**",
                    "/swagger-ui.html",
                    "/swagger-ui/**",
                    "/v3/api-docs/**"
                ).permitAll()

                // protegidos
                .pathMatchers(HttpMethod.GET, "/reviews/**").hasAuthority("ROLE_REVIEWS_READ")
                .pathMatchers(HttpMethod.POST, "/reviews/**").hasAuthority("ROLE_REVIEWS_WRITE")
                .pathMatchers(HttpMethod.PUT, "/reviews/**").hasAuthority("ROLE_REVIEWS_WRITE")
                .pathMatchers(HttpMethod.DELETE, "/reviews/**").hasAuthority("ROLE_REVIEWS_WRITE")

                .anyExchange().authenticated()
            )
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(
                    new ReactiveJwtAuthenticationConverterAdapter(jwtAuthenticationConverter()))
                )
            );

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            List<GrantedAuthority> authorities = new ArrayList<>();

            // realm roles
            Object realmAccessObj = jwt.getClaims().get("realm_access");
            if (realmAccessObj instanceof Map<?, ?> realmAccess) {
                Object rolesObj = realmAccess.get("roles");
                if (rolesObj instanceof Collection<?> roles) {
                    authorities.addAll(
                        roles.stream()
                             .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                             .collect(Collectors.toList())
                    );
                }
            }

            // client roles del cliente ms-res-client
            Object resourceAccessObj = jwt.getClaims().get("resource_access");
            if (resourceAccessObj instanceof Map<?, ?> resourceAccess) {
                Object clientAccessObj = resourceAccess.get("ms-res-client");
                if (clientAccessObj instanceof Map<?, ?> clientAccess) {
                    Object clientRolesObj = clientAccess.get("roles");
                    if (clientRolesObj instanceof Collection<?> roles) {
                        authorities.addAll(
                            roles.stream()
                                 .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                                 .collect(Collectors.toList())
                        );
                    }
                }
            }

            return authorities;
        });

        return converter;
    }
}
