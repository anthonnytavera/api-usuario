package com.adtavera.apiusuario.controllers;

import com.adtavera.apiusuario.dtos.LoginDto;
import com.adtavera.apiusuario.services.AuthService;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(
        name="Authentication",
        description = "Auth Controllers")
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    @SecurityRequirements
    public ResponseEntity<String> login(@RequestBody LoginDto loginDto) {
        String token = this.authService.login(loginDto);

        return ResponseEntity.ok(token);
    }
}
