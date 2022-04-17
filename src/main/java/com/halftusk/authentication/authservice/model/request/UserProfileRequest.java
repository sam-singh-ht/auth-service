package com.halftusk.authentication.authservice.model.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
public class UserProfileRequest {

    @NotBlank
    @Size(min = 3, max = 40)
    private String username;

    @NotBlank
    @Size(min = 8, max = 20)
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must contain at least one lowercase Latin character [a-z]. Password must contain at least one uppercase Latin character [A-Z]. Password must contain at least one special character like ! @ # & ( ). Password must contain a length of at least 8 characters and a maximum of 20 characters."
    )
    private String oldPassword;

    @NotBlank
    @Size(min = 8, max = 20)
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must contain at least one lowercase Latin character [a-z]. Password must contain at least one uppercase Latin character [A-Z]. Password must contain at least one special character like ! @ # & ( ). Password must contain a length of at least 8 characters and a maximum of 20 characters."
    )
    private String newPassword;

    @NotBlank
    @Size(min = 8, max = 20)
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must contain at least one lowercase Latin character [a-z]. Password must contain at least one uppercase Latin character [A-Z]. Password must contain at least one special character like ! @ # & ( ). Password must contain a length of at least 8 characters and a maximum of 20 characters."
    )
    private String confirmPassword;

    private String otp;

}
