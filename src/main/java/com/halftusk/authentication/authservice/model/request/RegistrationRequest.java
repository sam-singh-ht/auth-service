package com.halftusk.authentication.authservice.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@Validated
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationRequest {

    @NotBlank(message = "firstName can not be blank")
    @Size(min = 3, max = 40)
    private String firstName;

    @NotBlank(message = "lastName can not be blank")
    @Size(min = 3, max = 40)
    private String lastName;

    @NotBlank(message = "username can not be blank")
    @Size(min = 3, max = 15)
    private String username;

    @NotBlank(message = "email can not be blank")
    @Size(max = 40)
    @Email
    private String email;

    @NotBlank(message = "phoneNumber can not be blank")
    @Size(max = 40)
    private String phoneNumber;

    @NotBlank
    @Size(min = 6, max = 20)
    private String password;

    private boolean mfa;

    @NotBlank
    private String role;

}
