package org.wtc.application.user.controller;




import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.wtc.application.auth.entity.AuthenticableUser;
import org.wtc.application.user.dto.UpdateUserSegmentsRequestDTO;
import org.wtc.application.user.dto.UpdateUserTagRequestDto;
import org.wtc.application.user.dto.UserRequestDTO;
import org.wtc.application.user.dto.UserResponseDTO;
import org.wtc.application.user.service.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.wtc.application.user.service.UpdateUserSegments;

import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Usuários Internos", description = "Endpoints para cadastro, gerenciamento e controle de perfis de operadores e administradores do sistema.")
public class UserController {

    private final IUserService userService;
    private final UpdateUserSegments updateUserSegments;

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Cadastrar um novo usuário", description = "Registra um novo usuário interno (operador ou administrador) no sistema.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Usuário cadastrado com sucesso.",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Dados fornecidos na requisição são inválidos.", content = @Content)
    })
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO request, @AuthenticationPrincipal @Parameter(hidden = true) AuthenticableUser user) {
        UserResponseDTO response = userService.createUser(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PatchMapping("/{userId}/segments")
    @Operation(summary = "Atualizar segmentos vinculados ao usuário", description = "Modifica a associação de segmentos de atendimento sob responsabilidade deste usuário.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Segmentos atualizados com sucesso.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado.", content = @Content)
    })
    public ResponseEntity<Void> updateUserSegments(
            @PathVariable @Parameter(description = "ID do usuário", example = "1") Long userId,
            @RequestBody UpdateUserSegmentsRequestDTO request
    ) {
        updateUserSegments.updateUserSegments(userId, request);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Buscar usuário por ID", description = "Busca detalhada das informações cadastrais de um usuário interno pelo identificador único.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário localizado com sucesso.",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado.", content = @Content)
    })
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable @Parameter(description = "ID único do usuário", example = "1") Long id, @AuthenticationPrincipal @Parameter(hidden = true) AuthenticableUser user) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Listar todos os usuários", description = "Retorna uma lista contendo todos os operadores e administradores cadastrados.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista de usuários recuperada com sucesso.",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserResponseDTO.class))))
    })
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(@AuthenticationPrincipal @Parameter(hidden = true) AuthenticableUser user) {
        return ResponseEntity.ok(userService.findAll());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Atualizar dados do usuário", description = "Modifica as informações gerais e credenciais de um usuário interno existente.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso.",
                    content = @Content(schema = @Schema(implementation = UserResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Payload enviado contém dados inválidos.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado.", content = @Content)
    })

    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable @Parameter(description = "ID do usuário a ser editado", example = "1") Long id,
            @Valid @RequestBody UserRequestDTO request,
            @AuthenticationPrincipal @Parameter(hidden = true) AuthenticableUser user) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN')")
    @Operation(summary = "Deletar um usuário", description = "Remove do sistema o cadastro do usuário interno informado pelo ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Usuário removido com sucesso.", content = @Content),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado.", content = @Content)
    })
    public ResponseEntity<Void> deleteUser(@PathVariable @Parameter(description = "ID do usuário a ser removido", example = "1") Long id, @AuthenticationPrincipal @Parameter(hidden = true) AuthenticableUser user) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}