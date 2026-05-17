package org.wtc.application.tag.service;


import org.wtc.application.tag.dto.TagRequestDTO;
import org.wtc.application.tag.dto.TagResponseDTO;

import java.util.List;

public interface ITagService {
    TagResponseDTO createTag(TagRequestDTO request);
    TagResponseDTO findById(Long id);
    List<TagResponseDTO> findAll();
    TagResponseDTO updateTag(Long id, TagRequestDTO request);
    void deleteTag(Long id);
}
