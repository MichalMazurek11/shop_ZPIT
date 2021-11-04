package com.project.shop.model.dtos;


import lombok.*;
import org.hibernate.validator.constraints.UniqueElements;

import javax.persistence.Column;
import javax.validation.constraints.Email;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class UserDto {

    @Email
    @NotBlank(message = "Wpisz emaila")
    private String email;

    @NotBlank(message = "haslo")
    private String password;

    @NotBlank(message = "Wybierz")
    private String state;

    @NotBlank(message = "Wpisz miasto")
    private String city;

    @NotBlank(message = "Wpisz ulice")
    private String adress;


    @NotBlank(message = "Wpisz kod")
    private String zipCode;



}
