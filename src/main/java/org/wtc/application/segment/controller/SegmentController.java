package org.wtc.application.segment.controller;



import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.wtc.application.auth.entity.AuthenticableUser;
import org.wtc.application.segment.dto.SegmentRequestDTO;
import org.wtc.application.segment.dto.SegmentResponseDTO;
import org.wtc.application.segment.service.ISegmentService;

import java.util.List;

@RestController
@RequestMapping("/segments")
@RequiredArgsConstructor
public class SegmentController {

    private final ISegmentService segmentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('OPERATOR','ADMIN')")
    public ResponseEntity<SegmentResponseDTO> createSegment(@Valid @RequestBody SegmentRequestDTO request ,@AuthenticationPrincipal AuthenticableUser user)
                                                             {
        SegmentResponseDTO response = segmentService.createSegment(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERATOR','ADMIN')")
    public ResponseEntity<SegmentResponseDTO> getSegmentById(@PathVariable Long id,@AuthenticationPrincipal AuthenticableUser user ) {
        return ResponseEntity.ok(segmentService.findById(id));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('OPERATOR','ADMIN')")
    public ResponseEntity<Page<SegmentResponseDTO>> getAllSegments(@AuthenticationPrincipal AuthenticableUser user, Pageable pageable) {
        return ResponseEntity.ok(segmentService.findAll(pageable));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERATOR','ADMIN')")
    public ResponseEntity<SegmentResponseDTO> updateSegment(
            @PathVariable Long id,
            @Valid @RequestBody SegmentRequestDTO request,
            @AuthenticationPrincipal AuthenticableUser user) {
        return ResponseEntity.ok(segmentService.updateSegment(id, request));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('OPERATOR','ADMIN')")
    public ResponseEntity<Void> deleteSegment(@PathVariable Long id,
                                              @AuthenticationPrincipal AuthenticableUser user) {
        segmentService.deleteSegment(id);
        return ResponseEntity.noContent().build();
    }
}
