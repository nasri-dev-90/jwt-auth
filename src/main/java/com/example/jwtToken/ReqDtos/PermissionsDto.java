package com.example.jwtToken.ReqDtos;

public record PermissionsDto(
        String resource,
        String action
) {
}
