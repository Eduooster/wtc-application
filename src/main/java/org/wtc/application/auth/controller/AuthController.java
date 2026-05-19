package org.wtc.application.auth.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
@Tag(name = "Autenticação & Registro", description = "Endpoints responsáveis pelo controle de acesso, login e criação de novas contas no sistema (Usuários/Operadores e Clientes).")
public class AuthController {

    private final UserRegistrationService userRegistrationService;
    private final ClientRegistrationService clientRegistrationService;
    private final LoginService loginService;

    @PostMapping("/login")
    @Operation(
            summary = "Autenticar usuário/cliente",
            description = "Valida as credenciais enviadas (e-mail e senha) e retorna um token JWT válido para acessar os endpoints protegidos do sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Autenticação realizada com sucesso.",
                    content = @Content(schema = @Schema(implementation = TokenResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas (E-mail ou senha incorretos).", content = @Content),
            @ApiResponse(responseCode = "400", description = "Dados da requisição malformados ou inválidos.", content = @Content)
    })
    public ResponseEntity<TokenResponseDto> login(@RequestBody @Valid LoginRequestDto dto) {
        TokenResponseDto tokenResponse = loginService.authenticate(dto);
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/register/user")
    @Operation(
            summary = "Registrar um novo Usuário (Operador)",
            description = "Cria uma nova conta de usuário interno/operador no sistema com o perfil de acesso adequado."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário registrado com sucesso.",
                    content = @Content(schema = @Schema(implementation = RegisterUserResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Falha na validação dos dados de cadastro (ex: e-mail já existente ou senha fraca).", content = @Content)
    })
    public ResponseEntity<RegisterUserResponseDto> registerUser(@RequestBody @Valid UserRegistrationDto dto) {
        RegisterUserResponseDto registerUserResponseDto = userRegistrationService.register(dto);
        return ResponseEntity.ok(registerUserResponseDto);
    }

    @PostMapping("/register/client")
    @Operation(
            summary = "Registrar um novo Cliente",
            description = "Realiza o autocadastro ou registro de um novo cliente externo no sistema."
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente registrado com sucesso.",
                    content = @Content(schema = @Schema(implementation = RegisterClientResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Falha na validação dos dados do cliente.", content = @Content)
    })
    public ResponseEntity<RegisterClientResponseDto> registerClient(@RequestBody @Valid ClientRegistrationDto dto) {
        RegisterClientResponseDto registerClientResponseDto = clientRegistrationService.register(dto);
        return ResponseEntity.ok(registerClientResponseDto);
    }
}