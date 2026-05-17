package org.wtc.application.audit.controller;


import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.wtc.application.audit.dto.AuditRequestDTO;
import org.wtc.application.audit.dto.AuditResponseDTO;
import org.wtc.application.audit.service.IAuditService;
import org.wtc.application.auth.entity.AuthenticableUser;

import java.util.List;

@RestController
@RequestMapping("/audits")
@RequiredArgsConstructor
public class AuditController {

    private final IAuditService auditService;

    @PostMapping
    public ResponseEntity<AuditResponseDTO> createAudit(@Valid @RequestBody AuditRequestDTO request,@AuthenticationPrincipal AuthenticableUser user) {
        AuditResponseDTO response = auditService.createAudit(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuditResponseDTO> getAuditById(@PathVariable Long id,@AuthenticationPrincipal AuthenticableUser user) {
        return ResponseEntity.ok(auditService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<AuditResponseDTO>> getAllAudits() {
        return ResponseEntity.ok(auditService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuditResponseDTO> updateAudit(
            @PathVariable Long id,
            @Valid @RequestBody AuditRequestDTO request) {
        return ResponseEntity.ok(auditService.updateAudit(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAudit(@PathVariable Long id) {
        auditService.deleteAudit(id);
        return ResponseEntity.noContent().build();
    }
}