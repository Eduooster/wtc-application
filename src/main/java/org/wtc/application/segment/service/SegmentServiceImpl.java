package org.wtc.application.segment.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wtc.application.segment.dto.SegmentRequestDTO;
import org.wtc.application.segment.dto.SegmentResponseDTO;
import org.wtc.application.segment.entity.Segment;
import org.wtc.application.segment.mapper.SegmentMapper;
import org.wtc.application.segment.repository.SegmentRepository;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class SegmentServiceImpl implements ISegmentService {

    private final SegmentRepository repository;
    private final SegmentMapper mapper;

    @Override
    @Transactional
    public SegmentResponseDTO createSegment(SegmentRequestDTO request) {

        if (repository.existsByNameIgnoreCase(request.getName())) {
            throw new RuntimeException("Já existe um segmento com o nome: " + request.getName());
        }

        Segment segment = mapper.toEntiy(request);

        segment.setActive(true);
        segment.setDeleted(false);

        return mapper.toDto(repository.save(segment));
    }

    @Override
    public SegmentResponseDTO findById(Long id) {
        return repository.findByIdAndDeletedFalse(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new RuntimeException("Segmento não encontrado com o ID: " + id));
    }

    @Override
    public List<SegmentResponseDTO> findAll() {

        return repository.findAllByDeletedFalseAndActiveTrue().stream()
                .map(mapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SegmentResponseDTO updateSegment(Long id, SegmentRequestDTO request) {
        Segment segment = repository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Segmento não encontrado para atualização"));


        mapper.updateEntityFromDto(request, segment);

        return mapper.toDto(repository.save(segment));
    }

    @Override
    @Transactional
    public void deleteSegment(Long id) {
        Segment segment = repository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Segmento não encontrado"));


        segment.setDeleted(true);
        segment.setActive(false);
        repository.save(segment);
    }
}
