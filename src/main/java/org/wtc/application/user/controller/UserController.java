package org.wtc.application.user.controller;




import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.wtc.application.auth.entity.AuthenticableUser;
import org.wtc.application.user.dto.UpdateUserSegmentsRequestDTO;
import org.wtc.application.user.dto.UserRequestDTO;
import org.wtc.application.user.dto.UserResponseDTO;
import org.wtc.application.user.service.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.wtc.application.user.service.UpdateUserSegments;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;
    private final UpdateUserSegments updateUserSegments;

    @PostMapping
    public ResponseEntity<UserResponseDTO> createUser(@Valid @RequestBody UserRequestDTO request,@AuthenticationPrincipal AuthenticableUser user) {
        UserResponseDTO response = userService.createUser(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PatchMapping("/{userId}/segments")
    public ResponseEntity<Void> updateUserSegments(
            @PathVariable Long userId,
            @RequestBody UpdateUserSegmentsRequestDTO request
    ) {

        updateUserSegments.updateUserSegments(userId, request);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable Long id,@AuthenticationPrincipal AuthenticableUser user) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<UserResponseDTO>> getAllUsers(@AuthenticationPrincipal AuthenticableUser user) {
        return ResponseEntity.ok(userService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponseDTO> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UserRequestDTO request,
            @AuthenticationPrincipal AuthenticableUser user) {
        return ResponseEntity.ok(userService.updateUser(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id,@AuthenticationPrincipal AuthenticableUser user) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
