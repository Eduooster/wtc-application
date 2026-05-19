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
import org.wtc.application.auth.dto.*;
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
    public ResponseEntity<RegisterUserResponseDto> registerUser(@RequestBody @Valid UserRegistrationDto dto) {
        RegisterUserResponseDto registerUserResponseDto  = userRegistrationService.register(dto);
        return ResponseEntity.ok(registerUserResponseDto);
    }

    @PostMapping("/register/client")
    public ResponseEntity<RegisterClientResponseDto> registerClient(@RequestBody @Valid ClientRegistrationDto dto) {
        RegisterClientResponseDto registerClientResponseDto = clientRegistrationService.register(dto);
        return ResponseEntity.ok(registerClientResponseDto);
    }

    //    @PostMapping("/refresh")
//    public ResponseEntity<TokenResponseDto> refresh(@RequestBody @Valid RefreshTokenRequestDto dto) {
//        var tokenResponse = authService.refreshAccessToken(dto);
//        return ResponseEntity.ok(tokenResponse);
//    }
}