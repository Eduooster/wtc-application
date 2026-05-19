package org.wtc.application.client.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wtc.application.client.entity.Client;
import org.wtc.application.client.repository.ClientRepository;
import org.wtc.application.segment.entity.Segment;
import org.wtc.application.segment.repository.SegmentRepository;
import org.wtc.application.user.dto.UpdateUserSegmentsRequestDTO;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UpdateClientSegments {

    private final ClientRepository clientRepository;
    private final SegmentRepository segmentRepository;

    @Transactional
    public void updateClientSegments(Long userId, UpdateUserSegmentsRequestDTO request) {

        Client client =clientRepository.findById(userId).orElseThrow(() -> new RuntimeException("Client not found"));

        Set<Segment> segments = new HashSet<>(
                segmentRepository.findAllById(request.segmentIds())
        );
        client.setSegments(segments);

    }

}
