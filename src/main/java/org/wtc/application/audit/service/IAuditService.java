package org.wtc.application.audit.service;


import org.wtc.application.audit.dto.AuditRequestDTO;
import org.wtc.application.audit.dto.AuditResponseDTO;

import java.util.List;

public interface IAuditService {
    AuditResponseDTO createAudit(AuditRequestDTO request);
    AuditResponseDTO findById(Long id);
    List<AuditResponseDTO> findAll();
    AuditResponseDTO updateAudit(Long id, AuditRequestDTO request);
    void deleteAudit(Long id);
}