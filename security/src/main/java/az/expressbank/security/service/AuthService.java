package az.expressbank.security.service;

import az.expressbank.security.data.entity.UserCredentials;
import az.expressbank.security.data.enums.Role;
import az.expressbank.security.data.repository.UserCredentialsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserCredentialsRepository repository;
    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

    public String saveUser(UserCredentials credential) {
        credential.setPassword(passwordEncoder.encode(credential.getPassword()));
        repository.save(credential);
        return "user added to the system";
    }

    public String generateToken(String username, List<GrantedAuthority> roles) {
        return jwtService.generateToken(username, roles);
    }

    public void validateToken(String token) {
        jwtService.validateToken(token);
    }
}
