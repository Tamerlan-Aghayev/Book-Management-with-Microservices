package az.expressbank.security.controller;

import az.expressbank.security.data.dto.UserCredentialsDTO;
import az.expressbank.security.data.entity.UserCredentials;
import az.expressbank.security.mapper.UserCredentialsMapper;
import az.expressbank.security.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;
    private final UserCredentialsMapper userCredentialsMapper;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public String addNewUser(@RequestBody UserCredentialsDTO userDTO) {
        UserCredentials user=userCredentialsMapper.dtoToEntity(userDTO);
        return service.saveUser(user);
    }

    @PostMapping("/token")
    public ResponseEntity<String> getToken(@RequestParam("username") String username, @RequestParam("password") String password) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        if (authenticate.isAuthenticated()) {
            return ResponseEntity.ok(service.generateToken(username, (List<GrantedAuthority>) authenticate.getAuthorities()));
        } else {
            throw new RuntimeException("invalid access");
        }
    }

    @GetMapping("/validate")
    public ResponseEntity<String> validateToken(@RequestParam("token") String token) {
        service.validateToken(token);
        return ResponseEntity.ok("Valid");
    }
    @GetMapping("/extract")
    public List<String> extract(@RequestParam("token") String token) {

        return service.extractToken(token);
    }
}