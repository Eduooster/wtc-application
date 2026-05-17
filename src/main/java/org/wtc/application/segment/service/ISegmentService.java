package org.wtc.application.segment.service;

import org.wtc.application.segment.dto.SegmentRequestDTO;
import org.wtc.application.segment.dto.SegmentResponseDTO;

import java.util.List;

public interface ISegmentService {
    SegmentResponseDTO createSegment(SegmentRequestDTO request);
    SegmentResponseDTO findById(Long id);
    List<SegmentResponseDTO> findAll();
    SegmentResponseDTO updateSegment(Long id, SegmentRequestDTO request);
    void deleteSegment(Long id);
}