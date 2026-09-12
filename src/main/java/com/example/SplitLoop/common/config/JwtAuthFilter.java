package com.example.SplitLoop.common.config;

import com.example.SplitLoop.auth.domain.entity.Token;
import com.example.SplitLoop.auth.domain.repository.TokenRepository;
import com.example.SplitLoop.auth.domain.service.JwtService;
import com.example.SplitLoop.user.domain.model.CustomUserPrincipal;
import com.example.SplitLoop.user.domain.entity.User;
import com.example.SplitLoop.user.domain.repository.UserRepository;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // No validar JWT en endpoints de autenticación
        if (request.getServletPath().startsWith("/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);

        // Si no hay token, continuar (Spring Security decidirá si requiere autenticación)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {

            final String jwtToken = authHeader.substring(7).trim();

            if (jwtToken.isBlank()) {
                filterChain.doFilter(request, response);
                return;
            }

            final String userEmail = jwtService.extractUsername(jwtToken);

            if (userEmail == null ||
                    SecurityContextHolder.getContext().getAuthentication() != null) {

                filterChain.doFilter(request, response);
                return;
            }


            final Token token = tokenRepository.findByToken(jwtToken)
                    .orElse(null);

            if (token == null || token.hasExpired() || token.isRevoked()) {
                filterChain.doFilter(request, response);
                return;
            }

            final CustomUserPrincipal principal = (CustomUserPrincipal) userDetailsService.loadUserByUsername(userEmail);

            final Optional<User> user = userRepository.findByEmail(principal.getUsername());

            if (user.isEmpty()) {
                filterChain.doFilter(request, response);
                return;
            }

            final boolean isTokenValid = jwtService.isTokenValid(jwtToken, user.get());


            if (!isTokenValid) {
                filterChain.doFilter(request, response);
                return;
            }


            final UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(
                            principal,
                            null,
                            principal.getAuthorities()
                    );

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

            SecurityContextHolder.getContext().setAuthentication(authToken);

            filterChain.doFilter(request, response);

        } catch (JwtException | IllegalArgumentException e) {

            /*
             * JWT inválido:
             * - MalformedJwtException
             * - ExpiredJwtException
             * - SignatureException
             * - IllegalArgumentException
             *
             * No autenticamos al usuario y dejamos que Spring Security
             * gestione la respuesta.
             */
            filterChain.doFilter(request, response);
        }
    }
}