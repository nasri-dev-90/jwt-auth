package com.example.jwtToken.ReqDtos;

import java.time.LocalDate;

public record RegisterRequest(
        String firstName,
        String lastName,
        String phoneNumber,
        String nationalId,
        String password,
        String address,
        String fatherName,
        LocalDate birthDate,
        String userRole
) {}