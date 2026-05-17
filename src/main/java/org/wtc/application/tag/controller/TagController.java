package org.wtc.application.tag.controller;




import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.wtc.application.auth.entity.AuthenticableUser;
import org.wtc.application.tag.dto.TagRequestDTO;
import org.wtc.application.tag.dto.TagResponseDTO;
import org.wtc.application.tag.service.ITagService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tags")
@RequiredArgsConstructor
public class TagController {

    private final ITagService tagService;

    @PostMapping
    public ResponseEntity<TagResponseDTO> createTag(@Valid @RequestBody TagRequestDTO request,@AuthenticationPrincipal AuthenticableUser user) {
        TagResponseDTO response = tagService.createTag(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagResponseDTO> getTagById(@PathVariable Long id,@AuthenticationPrincipal AuthenticableUser user) {
        return ResponseEntity.ok(tagService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<TagResponseDTO>> getAllTags(@AuthenticationPrincipal AuthenticableUser user) {
        return ResponseEntity.ok(tagService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TagResponseDTO> updateTag(
            @PathVariable Long id,
            @Valid @RequestBody TagRequestDTO request,
            @AuthenticationPrincipal AuthenticableUser user) {
        return ResponseEntity.ok(tagService.updateTag(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id,@AuthenticationPrincipal AuthenticableUser user) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
}
