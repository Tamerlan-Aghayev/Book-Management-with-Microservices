package az.expressbank.security.service;

import az.expressbank.security.data.dto.UserCredentialsDTO;

public interface AuthService {
     String saveUser(UserCredentialsDTO credentialDTO);

     String generateToken(String username, String password);

     boolean checkToken(String header, String url);
}
