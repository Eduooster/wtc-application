package org.example.wtcapplication.tag.service;

import org.example.wtcapplication.tag.dto.TagRequestDTO;
import org.example.wtcapplication.tag.dto.TagResponseDTO;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TagServiceImpl implements ITagService{
    @Override
    public TagResponseDTO createTag(TagRequestDTO request) {
        return null;
    }

    @Override
    public TagResponseDTO findById(Long id) {
        return null;
    }

    @Override
    public List<TagResponseDTO> findAll() {
        return List.of();
    }

    @Override
    public TagResponseDTO updateTag(Long id, TagRequestDTO request) {
        return null;
    }

    @Override
    public void deleteTag(Long id) {

    }
}
