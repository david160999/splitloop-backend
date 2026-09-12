package com.example.SplitLoop.common.config;

import com.example.SplitLoop.auth.domain.entity.Token;
import com.example.SplitLoop.auth.domain.repository.TokenRepository;
import com.example.SplitLoop.auth.domain.service.JwtService;
import com.example.SplitLoop.user.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
@Profile("prod")
public class SecurityConfig {

    private final AuthenticationProvider authenticationProvider;
    private final TokenRepository tokenRepository;

    @Bean
    JwtAuthFilter jwtAuthFilter(JwtService jwtService, UserDetailsService userDetailsService, TokenRepository tokenRepository, UserRepository userRepository
    ) {
        return new JwtAuthFilter(jwtService, userDetailsService, tokenRepository, userRepository
        );
    }

    @Bean
    SecurityFilterChain security(HttpSecurity http, JwtAuthFilter jwtAuthFilter) throws Exception {

        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers("/auth/**")
                                .permitAll()
                                .anyRequest()
                                .authenticated())

                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout ->
                        logout.logoutUrl("/auth/logout")
                                .addLogoutHandler((request, response, authentication) -> {
                                    final var authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
                                    logout(authHeader);
                                })
                                .logoutSuccessHandler(((request, response, authentication) ->
                                        SecurityContextHolder.clearContext()))
                );

        return http.build();

    }

    private void logout(final String token) {

        if (token == null || !token.startsWith("Bearer ")) {
            throw new IllegalArgumentException("Invalid token");
        }

        String jwtToken = token.substring(7);

        Token foundToken = tokenRepository.findByToken(jwtToken)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        foundToken.setRevoked(true);

        tokenRepository.save(foundToken);
    }
}
