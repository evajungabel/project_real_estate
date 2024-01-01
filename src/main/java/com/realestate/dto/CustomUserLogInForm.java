package com.realestate.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class CustomUserLogInForm {

//    @NotBlank(message = "Username cannot be empty!")
    private String username;

//    @NotNull(message = "Password cannot be empty!")
    private String password;

//    @NotNull(message = "E-mail cannot be empty!")
    private String email;

}
