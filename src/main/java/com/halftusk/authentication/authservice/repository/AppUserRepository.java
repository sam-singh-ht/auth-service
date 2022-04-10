package com.halftusk.authentication.authservice.repository;

import com.halftusk.authentication.authservice.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {
	boolean existsByUsername(String username);
	boolean existsByEmail(String email);
	boolean existsByPhoneNumber(String phoneNumber);
	AppUser findAppUserByEmail(String email);
}
