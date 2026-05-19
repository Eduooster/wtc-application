package org.wtc.application.user.dto;

import java.util.Set;

public record UpdateUserSegmentsRequestDTO(
        Set<Long> segmentIds
) {
}