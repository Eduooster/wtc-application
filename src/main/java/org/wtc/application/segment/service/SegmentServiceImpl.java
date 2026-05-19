package org.wtc.application.segment.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.wtc.application.audit.entity.Audit;
import org.wtc.application.audit.repository.AuditRepository;
import org.wtc.application.auth.entity.AuthenticableUser;
import org.wtc.application.segment.dto.SegmentRequestDTO;
import org.wtc.application.segment.dto.SegmentResponseDTO;
import org.wtc.application.segment.entity.Segment;
import org.wtc.application.segment.mapper.SegmentMapper;
import org.wtc.application.segment.repository.SegmentRepository;
import org.wtc.application.user.entity.User;
import org.wtc.application.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@RequiredArgsConstructor
public class SegmentServiceImpl implements ISegmentService {

    private final SegmentRepository repository;
    private final AuditRepository auditRepository;
    private  final UserRepository userRepository;

    @Qualifier("segmentMapper")
    private final SegmentMapper mapper;

    @Override
    @Transactional
    public SegmentResponseDTO createSegment(SegmentRequestDTO request, Long userId) {

        log.info("user id: " + userId );

        if (repository.existsByNameIgnoreCase(request.getName())) {
            throw new RuntimeException("Já existe um segmento com o nome: " + request.getName());
        }

        User user = userRepository.findById(userId).orElseThrow(()-> new EntityNotFoundException("User not found"));

        Segment segment = mapper.toEntiy(request);

        segment.setActive(true);
        segment.setDeleted(false);

        Segment savedSegment = repository.save(segment);

        auditRepository.save(
                new Audit(
                        "SEGMENT_CREATED",
                        "Segment created with name: " + savedSegment.getName(),
                        user,
                        savedSegment.getId(),
                        "Segment",
                        false
                )
        );


        return mapper.toDto(savedSegment);
    }

    @Override
    public SegmentResponseDTO findById(Long id) {
        return repository.findByIdAndDeletedFalse(id)
                .map(mapper::toDto)
                .orElseThrow(() -> new RuntimeException("Segmento não encontrado com o ID: " + id));
    }

    @Override
    public Page<SegmentResponseDTO> findAll(Pageable pageable) {

        return repository.findAllByDeletedFalseAndActiveTrue(pageable)
                .map(mapper::toDto);
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
    public void deleteSegment(Long id,Long userId) {
        log.info("user id: " + userId );
        Segment segment = repository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Segmento não encontrado"));

        User user = userRepository.findById(userId).orElseThrow(()-> new EntityNotFoundException("User not found"));

        segment.setDeleted(true);
        segment.setActive(false);

        auditRepository.save(
                new Audit(
                        "SEGMENT_DELETED",
                        "Segment deleated with name: " + segment.getName(),
                        user,
                        segment.getId(),
                        "Segment",
                        false
                )
        );
        repository.save(segment);
    }
}
