package az.expressbank.security.service;


import org.springframework.security.core.GrantedAuthority;

import java.util.List;


public interface JWTService {
     String generateToken(String userName, List<GrantedAuthority> roles);
     List<String> extractRoles(String token);
     boolean checkToken(String authHeader, String url);

}
