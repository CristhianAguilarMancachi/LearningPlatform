package com.ucb.plataforma.gateway;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.ReactiveJwtAuthenticationConverter;
import org.springframework.security.web.server.SecurityWebFilterChain;
import reactor.core.publisher.Flux;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecurityConfig.class);

    @Value("${spring.security.oauth2.resourceserver.jwt.jwk-set-uri}")
    private String jwkSetUri;

    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        LOGGER.info("🔐 Configurando seguridad del Gateway con JWK URI: {}", jwkSetUri);

        http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .authorizeExchange(auth -> auth
                // ✅ Endpoints públicos
                .pathMatchers(
                    "/actuator/**",
                    "/eureka/**",
                    "/error/**",
                    "/health/**",

                    // Swagger del propio gateway o de ms-res
                    "/v3/api-docs/**",
                    "/swagger-ui.html",
                    "/swagger-ui/**",
                    "/openapi/**",

                    // Swagger del microservicio ms-res (permitir vía gateway)
                    "/ms-res/openapi/**",
                    "/ms-res/swagger-ui.html",
                    "/ms-res/swagger-ui/**"
                ).permitAll()

                // 🔒 Todo lo demás requiere token JWT válido
                .anyExchange().authenticated()
            )
            // Configuración JWT reactiva
            .oauth2ResourceServer(oauth2 -> oauth2
                .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthenticationConverter()))
            );

        return http.build();
    }

    @Bean
    public ReactiveJwtAuthenticationConverter jwtAuthenticationConverter() {
        ReactiveJwtAuthenticationConverter converter = new ReactiveJwtAuthenticationConverter();

        converter.setJwtGrantedAuthoritiesConverter(jwt -> {
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");

            if (realmAccess == null || realmAccess.get("roles") == null) {
                LOGGER.warn("⚠️ No se encontraron roles en realm_access del token JWT");
                return Flux.empty();
            }

            @SuppressWarnings("unchecked")
            Collection<String> roles = (Collection<String>) realmAccess.get("roles");

            Collection<GrantedAuthority> authorities = roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());

            LOGGER.debug("✅ Roles extraídos del JWT: {}", authorities);
            return Flux.fromIterable(authorities);
        });

        return converter;
    }

    @Bean
    public ReactiveJwtDecoder reactiveJwtDecoder() {
        LOGGER.info("🧩 Creando ReactiveJwtDecoder con JWK URI: {}", jwkSetUri);
        try {
            return NimbusReactiveJwtDecoder.withJwkSetUri(jwkSetUri).build();
        } catch (Exception e) {
            LOGGER.error("❌ Error creando ReactiveJwtDecoder con URI: {}", jwkSetUri, e);
            throw new IllegalStateException("Cannot create ReactiveJwtDecoder", e);
        }
    }
}
