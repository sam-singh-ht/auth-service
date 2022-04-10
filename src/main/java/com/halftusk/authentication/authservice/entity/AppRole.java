package com.halftusk.authentication.authservice.entity;

import lombok.*;

import javax.persistence.*;

@Entity(name = "roles")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AppRole {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    public AppRole(String name) {
        this.name = name;
    }
}
