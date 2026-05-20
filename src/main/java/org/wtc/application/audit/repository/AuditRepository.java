package org.wtc.application.audit.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.wtc.application.audit.dto.AuditResponseDTO;
import org.wtc.application.audit.entity.Audit;

public interface AuditRepository extends JpaRepository<Audit, Long> {

}
