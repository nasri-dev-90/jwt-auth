package com.example.jwtToken.ReqDtos;




import java.util.List;
import java.util.Set;

public record RolesRequest(
        String name,
        List<PermissionsDto> permissions
) {
}
