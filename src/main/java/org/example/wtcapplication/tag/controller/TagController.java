package org.example.wtcapplication.tag.controller;




import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.example.wtcapplication.tag.dto.TagRequestDTO;
import org.example.wtcapplication.tag.dto.TagResponseDTO;
import org.example.wtcapplication.tag.service.ITagService;
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
    public ResponseEntity<TagResponseDTO> createTag(@Valid @RequestBody TagRequestDTO request) {
        TagResponseDTO response = tagService.createTag(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TagResponseDTO> getTagById(@PathVariable Long id) {
        return ResponseEntity.ok(tagService.findById(id));
    }

    @GetMapping
    public ResponseEntity<List<TagResponseDTO>> getAllTags() {
        return ResponseEntity.ok(tagService.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TagResponseDTO> updateTag(
            @PathVariable Long id,
            @Valid @RequestBody TagRequestDTO request) {
        return ResponseEntity.ok(tagService.updateTag(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTag(@PathVariable Long id) {
        tagService.deleteTag(id);
        return ResponseEntity.noContent().build();
    }
}
