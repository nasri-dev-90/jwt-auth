package com.example.jwtToken.ReqDtos;

import java.util.List;

public record RoleListToUserDto(
        String nationalId,
        List<String> roles
) {
}
