package net.ilyass.jpademo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;  
import net.ilyass.jpademo.dto.AuthRequest;
import net.ilyass.jpademo.dto.AuthResponse;
import net.ilyass.jpademo.dto.ErrorResponse;
import net.ilyass.jpademo.dto.RegisterRequest;
import net.ilyass.jpademo.entity.User;
import net.ilyass.jpademo.service.AuthService;
import net.ilyass.jpademo.service.UserService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        try {
            User user = new User();
            user.setName(request.getName());
            user.setEmail(request.getEmail());
            user.setPassword(request.getPassword());
            user.setRole(request.getRole());
            
            User savedUser = userService.saveUser(user);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity
                .badRequest()
                .body(new ErrorResponse(e.getMessage()));
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }
}