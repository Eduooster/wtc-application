package org.wtc.application.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.wtc.application.auth.dto.ClientRegistrationDto;
import org.wtc.application.auth.dto.LoginRequestDto;
import org.wtc.application.auth.dto.TokenResponseDto;
import org.wtc.application.auth.dto.UserRegistrationDto;
import org.wtc.application.auth.service.ClientRegistrationService;
import org.wtc.application.auth.service.LoginService;

import org.wtc.application.auth.service.UserRegistrationService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRegistrationService userRegistrationService;
    private final ClientRegistrationService clientRegistrationService;
    private final LoginService loginService;


    @PostMapping("/login")
    public ResponseEntity<TokenResponseDto> login(@RequestBody @Valid LoginRequestDto dto) {
        TokenResponseDto tokenResponse=   loginService.authenticate(dto);
        return ResponseEntity.ok(tokenResponse);
    }


    @PostMapping("/register/user")
    public ResponseEntity<Void> registerUser(@RequestBody @Valid UserRegistrationDto dto) {
        userRegistrationService.register(dto);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/register/client")
    public ResponseEntity<Void> registerClient(@RequestBody @Valid ClientRegistrationDto dto) {
        clientRegistrationService.register(dto);
        return ResponseEntity.noContent().build();
    }

    //    @PostMapping("/refresh")
//    public ResponseEntity<TokenResponseDto> refresh(@RequestBody @Valid RefreshTokenRequestDto dto) {
//        var tokenResponse = authService.refreshAccessToken(dto);
//        return ResponseEntity.ok(tokenResponse);
//    }
}