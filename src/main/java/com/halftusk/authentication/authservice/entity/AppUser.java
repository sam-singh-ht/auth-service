package com.halftusk.authentication.authservice.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity(name = "users")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    @NotBlank
    @Size(max = 15)
    private String username;
    private String email;
    private String password;
    private boolean mfa;
    private boolean isActive;
    private boolean accountExpired;
    private boolean accountLocked;
    private String phoneNumber;

    private String role;
    private LocalDateTime createdDt;

    public AppUser(String firstName,
                   String lastName,
                   String username,
                   String email,
                   String password,
                   boolean mfa,
                   boolean isActive,
                   boolean accountExpired,
                   boolean accountLocked,
                   String phoneNumber,
                   String roles,
                   LocalDateTime createdDt) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.username = username;
        this.email = email;
        this.password = password;
        this.mfa = mfa;
        this.isActive = isActive;
        this.accountExpired = accountExpired;
        this.accountLocked = accountLocked;
        this.phoneNumber = phoneNumber;
        this.role = roles;
        this.createdDt = createdDt;
    }
}
