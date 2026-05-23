package com.contractmanagementsystem.security;

import com.contractmanagementsystem.model.User;
import com.contractmanagementsystem.repository.UserRepository;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter
        extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(

            HttpServletRequest request,

            HttpServletResponse response,

            FilterChain filterChain

    )
            throws ServletException,
            IOException {

        final String authHeader =
                request.getHeader(
                        "Authorization"
                );

        // No token present
        if (authHeader == null
                ||
                !authHeader.startsWith(
                        "Bearer "
                )) {

            filterChain.doFilter(
                    request,
                    response
            );

            return;
        }

        final String jwtToken =
                authHeader.substring(7);

        try {

            // Signature + Expiry validation
            String userId =
                    jwtService.extractUserId(
                            jwtToken
                    );

            // Avoid re-authentication
            if (userId != null
                    &&
                    SecurityContextHolder
                            .getContext()
                            .getAuthentication()
                            == null) {

                User user =
                        userRepository
                                .findById(
                                        userId
                                )
                                .orElse(null);

                // User deleted
                if (user == null) {

                    response.setStatus(
                            HttpServletResponse.SC_UNAUTHORIZED
                    );

                    return;
                }

                // Role changed or token expired
                boolean isValid =
                        jwtService.isTokenValid(
                                jwtToken,
                                user
                        );

                if (!isValid) {

                    response.setStatus(
                            HttpServletResponse.SC_UNAUTHORIZED
                    );

                    return;
                }

                // Minimal authenticated user
                AuthenticatedUser principal =

                        new AuthenticatedUser(

                                user.getId(),

                                user.getUserName(),

                                user.getEmailId(),

                                user.getRole()
                        );

                UsernamePasswordAuthenticationToken authToken =

                        new UsernamePasswordAuthenticationToken(

                                principal,

                                null,

                                List.of(
                                        new SimpleGrantedAuthority(
                                                "ROLE_"
                                                        +
                                                        user
                                                                .getRole()
                                                                .name()
                                        )
                                )
                        );

                authToken.setDetails(

                        new WebAuthenticationDetailsSource()
                                .buildDetails(
                                        request
                                )
                );

                SecurityContextHolder
                        .getContext()
                        .setAuthentication(
                                authToken
                        );
            }

        }

        catch (ExpiredJwtException ex) {

            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED
            );

            response.getWriter()
                    .write(
                            "Token Expired. Please login again."
                    );

            return;
        }

        catch (JwtException ex) {

            response.setStatus(
                    HttpServletResponse.SC_UNAUTHORIZED
            );

            response.getWriter()
                    .write(
                            "Invalid Token"
                    );

            return;
        }

        filterChain.doFilter(
                request,
                response
        );
    }
}
