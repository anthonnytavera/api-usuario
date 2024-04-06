package com.adtavera.apiusuario.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {
    private String email;

    private String password;
}
