package com.example.jwtToken.user;

import com.example.jwtToken.role.RoleDto;

import java.time.LocalDate;
import java.util.Set;

public record UserDto(
        String firstName,
        String lastName,
        String phoneNumber,
        String address,
        String fatherName,
        LocalDate birthDate,
        Set<RoleDto> roles
) {
}
