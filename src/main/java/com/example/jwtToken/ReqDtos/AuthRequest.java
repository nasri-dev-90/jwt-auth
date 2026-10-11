package com.example.jwtToken.ReqDtos;

public record AuthRequest(
        String nationalId,
        String password
) {
}
