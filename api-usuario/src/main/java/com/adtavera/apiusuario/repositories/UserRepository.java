package com.adtavera.apiusuario.repositories;

import com.adtavera.apiusuario.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByEmailAndIdNot(String email, UUID id);
}
