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

                // protegidos: cualquier método en /reviews/** requiere ROLE_USER
                .pathMatchers("/reviews/**").hasAuthority("ROLE_USER")

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

            // 1) Roles de realm: realm_access.roles
            Object realmAccessObj = jwt.getClaims().get("realm_access");
            if (realmAccessObj instanceof Map<?, ?> realmAccess) {
                Object rolesObj = realmAccess.get("roles");
                if (rolesObj instanceof Collection<?> roles) {
                    authorities.addAll(
                        roles.stream()
                             // OJO: ya NO agregamos "ROLE_" delante,
                             // el rol en el token debe venir como "ROLE_USER"
                             .map(role -> new SimpleGrantedAuthority(role.toString()))
                             .collect(Collectors.toList())
                    );
                }
            }

            // 2) Roles de TODOS los clients: resource_access.<clientId>.roles
            Object resourceAccessObj = jwt.getClaims().get("resource_access");
            if (resourceAccessObj instanceof Map<?, ?> resourceAccess) {
                resourceAccess.forEach((clientId, clientAccessObj) -> {
                    if (clientAccessObj instanceof Map<?, ?> clientAccess) {
                        Object clientRolesObj = clientAccess.get("roles");
                        if (clientRolesObj instanceof Collection<?> roles) {
                            authorities.addAll(
                                roles.stream()
                                     .map(role -> new SimpleGrantedAuthority(role.toString()))
                                     .collect(Collectors.toList())
                            );
                        }
                    }
                });
            }

            return authorities;
        });

        return converter;
    }
}
