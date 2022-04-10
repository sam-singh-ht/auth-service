package com.halftusk.authentication.authservice.model.request;

import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Data
@ToString
public class LoginRequest {
	@NotBlank
	private String username;

	@NotBlank
	private String password;

}
