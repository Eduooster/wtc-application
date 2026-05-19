package org.wtc.application.client.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.wtc.application.auth.entity.AuthenticableUser;
import org.wtc.application.client.dto.ClientRequestDTO;
import org.wtc.application.client.dto.ClientResponseDTO;
import org.wtc.application.client.service.IClientService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.wtc.application.client.service.UpdateClientSegments;
import org.wtc.application.client.service.UpdateFireBaseToken;
import org.wtc.application.integration.fireBase.ClientFirebaseTokenService;
import org.wtc.application.integration.fireBase.DeviceTokenDto;
import org.wtc.application.integration.fireBase.FireBaseTokenRequestDto;
import org.wtc.application.user.dto.UpdateUserSegmentsRequestDTO;


import java.util.List;

@RestController
@RequestMapping("/clients")
@RequiredArgsConstructor
public class ClientController {

    private final IClientService clientService;
    private final UpdateClientSegments updateSegments;
    private final ClientFirebaseTokenService clientFirebaseTokenService;
    private final UpdateFireBaseToken updateFireBaseToken;

    @PostMapping
    public ResponseEntity<ClientResponseDTO> createClient(@Valid @RequestBody ClientRequestDTO request,@AuthenticationPrincipal AuthenticableUser user) {
        ClientResponseDTO response = clientService.createClient(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);


    }

    @PatchMapping("/{clientId}/segments")
    public ResponseEntity<Void> updateUserSegments(
            @PathVariable Long clientId,
            @RequestBody UpdateUserSegmentsRequestDTO request
    ) {

        updateSegments.updateClientSegments(clientId, request);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/firebase-token")
    public ResponseEntity<Void> saveFirebaseToken(
            @AuthenticationPrincipal AuthenticableUser principal,
            @RequestBody FireBaseTokenRequestDto dto
    ) {

        clientFirebaseTokenService.saveToken(principal, dto);

        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{id}/device-token")
    public ResponseEntity<Void> updateDeviceToken(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthenticableUser authenticableUser,
            @RequestBody @Valid DeviceTokenDto dto) {

        updateFireBaseToken.updateFirebaseToken(id,dto);

        return ResponseEntity.noContent().build();

    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> getClientById(@PathVariable Long id,@AuthenticationPrincipal AuthenticableUser user) {
        return ResponseEntity.ok(clientService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<ClientResponseDTO>> getAllClients(@AuthenticationPrincipal AuthenticableUser user) {
        return ResponseEntity.ok(clientService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClientResponseDTO> updateClient(
            @PathVariable Long id,
            @Valid @RequestBody ClientRequestDTO request,
            @AuthenticationPrincipal AuthenticableUser user) {
        return ResponseEntity.ok(clientService.updateClient(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id,@AuthenticationPrincipal AuthenticableUser user
    ) {
        clientService.deleteClient(id);
        return ResponseEntity.noContent().build();
    }
}