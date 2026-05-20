package org.wtc.application.audit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.wtc.application.audit.dto.AuditRequestDTO;
import org.wtc.application.audit.dto.AuditResponseDTO;
import org.wtc.application.audit.entity.Audit;
import org.wtc.application.audit.repository.AuditRepository;
import org.wtc.application.user.entity.User;
import org.wtc.application.user.repository.UserRepository;

import java.util.List;
@Service
@RequiredArgsConstructor
public class AuditServiceImpl implements IAuditService {

    private final AuditRepository auditRepository;
    private final UserRepository userRepository;

    @Override
    public AuditResponseDTO createAudit(AuditRequestDTO request) {

        Audit audit = new Audit();

        audit.setAction(request.getAction());
        audit.setDetails(request.getDetails());

        audit.setEntityId(request.getEntityId());
        audit.setEntityName(request.getEntityName());

        Audit savedAudit = auditRepository.save(audit);

        return mapToResponse(savedAudit);
    }

    @Override
    public AuditResponseDTO findById(Long id) {

        Audit audit = auditRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Audit not found"));

        return mapToResponse(audit);
    }

    @Override
    public Page<AuditResponseDTO> findAll(Pageable pageable) {



        return auditRepository.findAll(pageable)
                .map(this::mapToResponse);
    }

    @Override
    public AuditResponseDTO updateAudit(Long id, AuditRequestDTO request) {

        Audit audit = auditRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Audit not found"));

        audit.setAction(request.getAction());
        audit.setDetails(request.getDetails());

        audit.setEntityId(request.getEntityId());
        audit.setEntityName(request.getEntityName());

        Audit updatedAudit = auditRepository.save(audit);

        return mapToResponse(updatedAudit);
    }

    @Override
    public void deleteAudit(Long id) {

        Audit audit = auditRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Audit not found"));

        auditRepository.delete(audit);
    }

    private AuditResponseDTO mapToResponse(Audit audit) {

        return AuditResponseDTO.builder()
                .id(audit.getId())
                .action(audit.getAction())
                .details(audit.getDetails())
                .entityId(audit.getEntityId())
                .entityName(audit.getEntityName())
                .build();
    }
}