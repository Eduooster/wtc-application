package org.example.wtcapplication.tag.service;


import org.example.wtcapplication.tag.dto.TagRequestDTO;
import org.example.wtcapplication.tag.dto.TagResponseDTO;

import java.util.List;

public interface ITagService {
    TagResponseDTO createTag(TagRequestDTO request);
    TagResponseDTO findById(Long id);
    List<TagResponseDTO> findAll();
    TagResponseDTO updateTag(Long id, TagRequestDTO request);
    void deleteTag(Long id);
}
