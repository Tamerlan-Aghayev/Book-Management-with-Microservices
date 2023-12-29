package az.expressbank.security.controller;

import az.expressbank.security.data.dto.UserCredentialsDTO;
import az.expressbank.security.service.impl.AuthServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthServiceImpl service;


    @PostMapping("/register")
    public ResponseEntity<String> addNewUser(@RequestBody UserCredentialsDTO userDTO) {
        return ResponseEntity.ok(service.saveUser(userDTO));
    }

    @PostMapping("/token")
    public ResponseEntity<String> getToken(@RequestParam("username") String username, @RequestParam("password") String password) {
            return ResponseEntity.ok(service.generateToken(username, password));

    }

    @PostMapping("/check-token")
    public ResponseEntity<Boolean> checkToken(@RequestParam("header")String header, @RequestParam("url") String url) {
        return ResponseEntity.ok(service.checkToken(header, url));
    }
}