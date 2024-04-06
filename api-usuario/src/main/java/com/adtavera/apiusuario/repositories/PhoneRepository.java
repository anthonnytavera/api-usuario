package com.adtavera.apiusuario.repositories;

import com.adtavera.apiusuario.models.Phone;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PhoneRepository extends JpaRepository<Phone, UUID> {

    void deleteAllByUserId(UUID userId);
}
