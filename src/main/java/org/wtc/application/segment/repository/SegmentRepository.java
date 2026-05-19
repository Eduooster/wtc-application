package org.wtc.application.segment.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.wtc.application.segment.entity.Segment;

import java.util.List;
import java.util.Optional;

public interface SegmentRepository extends JpaRepository<Segment, Long> {
    Optional<Segment> findByIdAndDeletedFalse(Long id);

    Page<Segment> findAllByDeletedFalseAndActiveTrue(Pageable pageable);

    boolean existsByNameIgnoreCase(String name);
}
