package com.example.jwtToken.ReqDtos;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

public record UpdateProfileRequest(
        String nationalId,
        String firstName,
        String lastName,
        String phoneNumber,
        String address,
        String fatherName,
        LocalDate birthDate

) {
}
