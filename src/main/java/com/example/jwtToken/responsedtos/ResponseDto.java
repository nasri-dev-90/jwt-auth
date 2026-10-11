package com.example.jwtToken.responsedtos;

import java.time.LocalDateTime;
import java.util.List;

public record ResponseDto(
        int status,
        String message,
        LocalDateTime timestamp,
        Object data,
        List<String> errors
) {
}
