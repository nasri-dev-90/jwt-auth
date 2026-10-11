package com.example.jwtToken.role;

import com.example.jwtToken.Permission.PermissionDto;

import java.util.Set;

public record RoleDto(
        String name,
        Set<PermissionDto> permissions
) {
}
