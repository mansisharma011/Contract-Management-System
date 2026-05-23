package com.contractmanagementsystem.security;

import com.contractmanagementsystem.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secretKey;

    public String generateToken(
            User user
    ) {

        return Jwts.builder()

                .setSubject(user.getId())
                .claim("role", user.getRole().name())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(
                        getSignInKey(),
                        SignatureAlgorithm.HS256
                )
                .compact();
    }

    private Key getSignInKey() {

        byte[] keyBytes = Decoders.BASE64.decode(
                secretKey
        );

        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUserId(
            String token
    ) {

        return extractClaim(
                token,
                Claims::getSubject
        );
    }
    public String extractRole(
            String token
    ) {

        return extractClaim(
                token,
                claims ->
                        claims.get(
                                "role",
                                String.class
                        )
        );
    }
    public Date extractExpiration(
            String token
    ) {

        return extractClaim(
                token,
                Claims::getExpiration
        );
    }
    public <T> T extractClaim(
            String token,
            Function<Claims, T> resolver
    ) {

        Claims claims =
                extractAllClaims(token);

        return resolver.apply(claims);
    }
    private Claims extractAllClaims(
            String token
    ) {

        return Jwts.parserBuilder()

                .setSigningKey(
                        getSignInKey()
                )

                .build()

                .parseClaimsJws(
                        token
                )

                .getBody();
    }

    public boolean isTokenExpired(
            String token
    ) {

        return extractExpiration(token)
                .before(new Date());
    }

    public boolean isTokenValid(
            String token,
            User user
    ) {

        String tokenRole =
                extractRole(token);

        return tokenRole.equals(
                user.getRole().name()
        )

                &&

                !isTokenExpired(token);
    }
}
