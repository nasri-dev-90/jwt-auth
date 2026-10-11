package com.example.jwtToken.auth;

import com.example.jwtToken.ReqDtos.AuthRequest;
import com.example.jwtToken.ReqDtos.RegisterRequest;
import com.example.jwtToken.ReqDtos.TokenRefreshRequest;
import com.example.jwtToken.jwt.JwtService;
import com.example.jwtToken.refreshtoken.RefreshTokenService;
import com.example.jwtToken.refreshtoken.RefreshToken;
import com.example.jwtToken.role.Role;
import com.example.jwtToken.role.RoleRepository;
import com.example.jwtToken.user.User;
import com.example.jwtToken.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationManager authenticationManager;
    private final RoleRepository roleRepository;


    @Transactional
    public AuthResponse register(RegisterRequest request) {
        // بررسی تکراری نبودن نام کاربری
        if (repository.findByNationalId(request.nationalId()).isPresent()) {
            throw new RuntimeException("User already exists");
        }
        var user = User.builder()
                .firstName(request.firstName())
                .lastName(request.lastName())
                .phoneNumber(request.phoneNumber())
                .nationalId(request.nationalId())
                .password(passwordEncoder.encode(request.password()))
                .address(request.address())
                .fatherName(request.fatherName())
                .birthDate(request.birthDate())
                .membershipDate(LocalDate.now()) // تاریخ عضویت خودکار ثبت می‌شود
                .build();
        repository.save(user);

        var jwtToken = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user.getId());

        return new AuthResponse(jwtToken, refreshToken.getToken());
    }

    @Transactional
    public AuthResponse login(AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.nationalId(), request.password())
        );


        var user = repository.findByNationalId(request.nationalId()).orElseThrow();
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user.getId());

        return new AuthResponse(jwtToken, refreshToken.getToken());
    }

    public AuthResponse refreshToken(TokenRefreshRequest request) {
        return refreshTokenService.findByToken(request.refreshToken())
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtService.generateToken(user);
                    return new AuthResponse(token, request.refreshToken());
                })
                .orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));
    }
}