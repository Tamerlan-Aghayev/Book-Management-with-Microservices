package az.expressbank.security.service.impl;

import az.expressbank.security.exception.InvalidTokenException;
import az.expressbank.security.exception.UnauthorizedRoleException;
import az.expressbank.security.service.JWTService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class JwtServiceImpl implements JWTService {

    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";

    private void validateToken(final String token) {
        try {
            Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
            log.debug("Token validation successful");
        } catch (Exception e) {
            log.error("Error validating token: {}", e.getMessage());
            throw new InvalidTokenException("Either there is no token or it is invalid");
        }
    }

    public String generateToken(String userName, List<GrantedAuthority> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);

        String token = createToken(claims, userName);
        log.info("Token generated for user: {}", userName);
        return token;
    }

    private String createToken(Map<String, Object> claims, String userName) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userName)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public List<String> extractRoles(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        List<Map<String, String>> authorities = claims.get("roles", List.class);

        List<String> roles = authorities.stream()
                .map(authorityMap -> authorityMap.get("authority"))
                .collect(Collectors.toList());

        log.debug("Extracted roles from token: {}", roles);
        return roles;
    }

    public boolean checkToken(String authHeader, String url) {
        if (authHeader != null && authHeader.startsWith("Bearer")) {
            String token = authHeader.substring(7);

            try {
                validateToken(token);
            } catch (Exception exception) {
                log.error("Error checking token: {}", exception.getMessage());
                throw new InvalidTokenException("Either there is no token or it is invalid");
            }
            List<String> roles = extractRoles(token);

            if (url.startsWith("/book/") && hasUserRole(roles, "ROLE_USER")) {
                log.info("User with roles {} has access to URL {}", roles, url);
                return true;
            } else if (url.startsWith("/category/") && hasUserRole(roles, "ROLE_ADMIN")) {
                log.info("User with roles {} has access to URL {}", roles, url);
                return true;
            } else {
                log.warn("Unauthorized access attempt to URL {}", url);
                throw new UnauthorizedRoleException("Unauthorized access to the requested path");
            }
        }

        log.error("Invalid token format or missing token");
        throw new InvalidTokenException("Either there is no token or it is invalid");
    }

    private boolean hasUserRole(List<String> roles, String targetRole) {
        return roles.contains(targetRole);
    }
}
