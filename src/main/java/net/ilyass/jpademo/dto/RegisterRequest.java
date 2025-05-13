package net.ilyass.jpademo.dto;

import lombok.Data;
import net.ilyass.jpademo.entity.Role;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String password;
    private Role role;
}
