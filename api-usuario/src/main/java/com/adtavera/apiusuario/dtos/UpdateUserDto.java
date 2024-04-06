package com.adtavera.apiusuario.dtos;

import com.adtavera.apiusuario.models.Phone;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UpdateUserDto {

    @NotNull(message = "Name required")
    @NotBlank(message = "Name required")
    private String name;

    @NotNull(message = "Email required")
    @NotBlank(message = "Email required")
    @Email(message = "Invalid Email")
    private String email;

    @NotNull(message = "Password required")
    @NotBlank(message = "Password required")
    private String password;

    private List<Phone> phones = new ArrayList<>();

    private boolean isActive;
}
