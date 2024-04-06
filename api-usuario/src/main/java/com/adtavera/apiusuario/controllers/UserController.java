package com.adtavera.apiusuario.controllers;

import com.adtavera.apiusuario.dtos.CreateUserDto;
import com.adtavera.apiusuario.dtos.UpdateUserDto;
import com.adtavera.apiusuario.models.User;
import com.adtavera.apiusuario.services.UserService;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@Tag(
        name="Users",
        description = "User Controllers")
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        List<User> users = this.userService.findAll();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(
            @PathVariable("id")
            @Parameter(
                    name="id",
                    description = "User Id",
                    example = "550e8400-e29b-41d4-a716-446655440000") UUID id
    ) {
        User user = this.userService.findById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/register")
    @SecurityRequirements
    public ResponseEntity<User> createUser(@Valid @RequestBody CreateUserDto createUserDto) {
        User user = this.userService.create(createUserDto);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(
            @PathVariable("id")
            @Parameter(
                    name="id",
                    description = "User Id",
                    example = "550e8400-e29b-41d4-a716-446655440000") UUID id,
            @Valid @RequestBody UpdateUserDto updateUserDto) {
        User user = this.userService.update(id, updateUserDto);
        return ResponseEntity.ok(user);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable("id")
            @Parameter(
                    name="id",
                    description = "User Id",
                    example = "550e8400-e29b-41d4-a716-446655440000") UUID id
    ) {
        this.userService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
