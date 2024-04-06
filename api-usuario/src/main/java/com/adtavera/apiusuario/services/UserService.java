package com.adtavera.apiusuario.services;

import com.adtavera.apiusuario.dtos.UpdateUserDto;
import com.adtavera.apiusuario.dtos.CreateUserDto;
import com.adtavera.apiusuario.exceptions.BadRequestException;
import com.adtavera.apiusuario.exceptions.NotFoundException;
import com.adtavera.apiusuario.models.Phone;
import com.adtavera.apiusuario.models.User;
import com.adtavera.apiusuario.repositories.PhoneRepository;
import com.adtavera.apiusuario.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PhoneRepository phoneRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public User findById(UUID id) {
        return this.userRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "User with ID: " + id + " not found."));
    }

    public List<User> findAll() {
        return this.userRepository.findAll();
    }

    public User create(CreateUserDto createUserDto) {
        User checkEmail = this.userRepository
                .findUserByEmail(createUserDto.getEmail())
                .orElse(null);

        if (checkEmail != null) {
            throw new BadRequestException("Email: "
                    + createUserDto.getEmail()
                    + " is regitered.");
        }

        User user = new User();
        user.setName(createUserDto.getName());
        user.setEmail(createUserDto.getEmail());
        user.setPassword(this.passwordEncoder.encode(createUserDto.getPassword()));
        user.setActive(true);

        User userSaved = this.userRepository.save(user);

        List<Phone> phones = new ArrayList<>();
        createUserDto.getPhones().forEach(phoneDto -> {
            Phone phone = new Phone();
            phone.setNumber(phoneDto.getNumber());
            phone.setCityCode(phoneDto.getCityCode());
            phone.setCountryCode(phoneDto.getCountryCode());
            phone.setUser(userSaved);
            Phone phoneSaved = this.phoneRepository.save(phone);
            phones.add(phoneSaved);
        });

        userSaved.setPhones(phones);

        return userSaved;
    }

    public User update(UUID id, UpdateUserDto updateUserDto) {
        User currentUser = this.userRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "User with ID: " + id + " not found."));

        User checkEmail = this.userRepository
                .findUserByEmailAndIdNot(updateUserDto.getEmail(), id)
                .orElse(null);

        if (checkEmail != null) {
            throw new BadRequestException(
                    "Email: "
                            + updateUserDto.getEmail()
                            + " is registered.");
        }

        currentUser.setName(updateUserDto.getName());
        currentUser.setEmail(updateUserDto.getEmail());
        currentUser.setPassword(updateUserDto.getPassword());
        currentUser.setActive(updateUserDto.isActive());

        this.phoneRepository.deleteAllByUserId(currentUser.getId());

        currentUser.getPhones().forEach(phoneDto -> {
            Phone phone = new Phone();
            phone.setNumber(phoneDto.getNumber());
            phone.setCityCode(phoneDto.getCityCode());
            phone.setCountryCode(phoneDto.getCountryCode());
            phone.setUser(currentUser);
            this.phoneRepository.save(phone);
        });

        return this.userRepository.save(currentUser);
    }

    public void deleteById(UUID id) {
        this.userRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(
                        "User with ID: " + id + " not found."));

        this.userRepository.deleteById(id);
    }
}
