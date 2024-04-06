package com.adtavera.apiusuario.dtos;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class PhoneDto {
    @NotNull(message = "Number required")
    @NotBlank(message = "Number required")
    private String number;

    @NotNull(message = "City Code required")
    @NotBlank(message = "City Code required")
    private String cityCode;

    @NotNull(message = "Country Code required")
    @NotBlank(message = "Country Code required")
    private String countryCode;
}
