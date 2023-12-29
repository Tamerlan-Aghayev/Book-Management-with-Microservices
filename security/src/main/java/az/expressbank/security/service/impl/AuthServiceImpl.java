package az.expressbank.security.service.impl;

import az.expressbank.security.data.dto.UserCredentialsDTO;
import az.expressbank.security.data.repository.UserCredentialsRepository;
import az.expressbank.security.exception.WrongCredentialsException;
import az.expressbank.security.mapper.UserCredentialsMapper;
import az.expressbank.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {
    private final PasswordEncoder passwordEncoder;
    private final UserCredentialsRepository repository;
    private final UserCredentialsMapper userCredentialsMapper;

    private final AuthenticationManager authenticationManager;
    private final JwtServiceImpl jwtService;

    public String saveUser(UserCredentialsDTO credentialDTO) {
        credentialDTO.setPassword(passwordEncoder.encode(credentialDTO.getPassword()));
        repository.save(userCredentialsMapper.dtoToEntity(credentialDTO));
        log.info("User added to the system: {}", credentialDTO.getUsername());
        return "User added to the system";
    }

    public String generateToken(String username, String password) {
        Authentication authenticate;
        authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));

        if (authenticate.isAuthenticated()) {
            log.info("Token generated for user: {}", username);
            return jwtService.generateToken(username, (List<GrantedAuthority>) authenticate.getAuthorities());
        } else {
            log.warn("Failed to generate token for user: {}", username);
            throw new WrongCredentialsException("Wrong username or password");
        }
    }

    public boolean checkToken(String header, String url) {
        return jwtService.checkToken(header, url);
    }
}
