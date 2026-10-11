package com.example.jwtToken.Permission;

public record PermissionDto(
        String resource,
        String action
) {
}
