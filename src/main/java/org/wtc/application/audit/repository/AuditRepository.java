package org.wtc.application.audit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.wtc.application.audit.entity.Audit;

public interface AuditRepository extends JpaRepository<Audit, Long> {
}
