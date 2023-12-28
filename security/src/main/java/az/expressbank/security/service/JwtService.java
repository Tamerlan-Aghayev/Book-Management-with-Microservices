package az.expressbank.security.service;

import az.expressbank.security.data.enums.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import java.security.Key;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtService {


    public static final String SECRET = "5367566B59703373367639792F423F4528482B4D6251655468576D5A71347437";


    public void validateToken(final String token) {
        Jwts.parserBuilder().setSigningKey(getSignKey()).build().parseClaimsJws(token);
    }


    public String generateToken(String userName, List<GrantedAuthority> roles) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roles);

        return createToken(claims, userName);
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

        return authorities.stream()
                .map(authorityMap -> authorityMap.get("authority"))
                .collect(Collectors.toList());
    }

    public boolean checkToken(String authHeader, String url) {
        System.out.println(authHeader);
        System.out.println(url);

        if (authHeader != null && authHeader.startsWith("Bearer")) {
            String token = authHeader.substring(7);
            List<String> roles = extractRoles(token);

            try {
                System.out.println("in try");
                if (url.startsWith("/book/") && hasUserRole(roles, "ROLE_USER")) {
                    return true;
                } else if (url.startsWith("/category/") && hasUserRole(roles, "ROLE_ADMIN")) {
                    return true;
                } else {
                    throw new RuntimeException("Unauthorized access to the requested path");
                }
            } catch (Exception e) {
                System.out.println("Invalid access...!");
                e.printStackTrace();
            }
        }
        return false;
    }
    private boolean hasUserRole(List<String> roles, String targetRole) {

        return roles.contains(targetRole);
    }
}