package org.wtc.application.segment.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.wtc.application.segment.dto.SegmentRequestDTO;
import org.wtc.application.segment.dto.SegmentResponseDTO;

import java.util.List;

public interface ISegmentService {
    SegmentResponseDTO createSegment(SegmentRequestDTO request);
    SegmentResponseDTO findById(Long id);
    Page<SegmentResponseDTO> findAll(Pageable pageable);
    SegmentResponseDTO updateSegment(Long id, SegmentRequestDTO request);
    void deleteSegment(Long id);
}