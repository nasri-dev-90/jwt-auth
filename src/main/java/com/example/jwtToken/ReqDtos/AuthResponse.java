package com.example.jwtToken.ReqDtos;

public record AuthResponse(
        String accessToken,
        String refreshToken
) {
}
