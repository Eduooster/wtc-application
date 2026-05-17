package org.wtc.application.audit.service;

import org.springframework.stereotype.Service;
import org.wtc.application.audit.dto.AuditRequestDTO;
import org.wtc.application.audit.dto.AuditResponseDTO;

import java.util.List;

@Service
public class AuditServiceImpl implements IAuditService {
    @Override
    public AuditResponseDTO createAudit(AuditRequestDTO request) {
        return null;
    }

    @Override
    public AuditResponseDTO findById(Long id) {
        return null;
    }

    @Override
    public List<AuditResponseDTO> findAll() {
        return List.of();
    }

    @Override
    public AuditResponseDTO updateAudit(Long id, AuditRequestDTO request) {
        return null;
    }

    @Override
    public void deleteAudit(Long id) {

    }
}
