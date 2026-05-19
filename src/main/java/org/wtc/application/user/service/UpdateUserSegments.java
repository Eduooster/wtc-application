package org.wtc.application.user.service;


import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wtc.application.segment.entity.Segment;
import org.wtc.application.segment.repository.SegmentRepository;
import org.wtc.application.user.dto.UpdateUserSegmentsRequestDTO;
import org.wtc.application.user.entity.User;
import org.wtc.application.user.repository.UserRepository;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UpdateUserSegments {

    private final SegmentRepository segmentRepository;
    private final UserRepository userRepository;


    @Transactional
    public void updateUserSegments(
            Long userId,
            UpdateUserSegmentsRequestDTO request
    ) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));



        Set<Segment> segments = new HashSet<>(
                segmentRepository.findAllById(request.segmentIds())
        );

        user.setSegments(segments);


    }
}
