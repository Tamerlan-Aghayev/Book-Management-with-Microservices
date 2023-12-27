package az.expressbank.security.data.dto;

import az.expressbank.security.data.enums.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCredentialsDTO {
    private String username;
    private String password;
    private Set<Role> roles;
}