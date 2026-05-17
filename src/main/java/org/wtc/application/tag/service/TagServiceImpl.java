package org.wtc.application.tag.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.wtc.application.tag.dto.TagRequestDTO;
import org.wtc.application.tag.dto.TagResponseDTO;
import org.springframework.stereotype.Service;
import org.wtc.application.tag.entity.Tag;
import org.wtc.application.tag.mapper.TagMapper;
import org.wtc.application.tag.repository.TagRepository;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class TagServiceImpl implements ITagService {

    private final TagRepository repository;
    @Qualifier("tagMapper")
    private final TagMapper mapper;

    @Override
    @Transactional
    public TagResponseDTO createTag(TagRequestDTO request) {
        Tag tag = mapper.toEntity(request);
        tag.setDeleted(false);
        return mapper.toDTO(repository.save(tag));
    }

    @Override
    public TagResponseDTO findById(Long id) {
        return repository.findByIdAndDeletedFalse(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new RuntimeException("Tag não encontrada"));
    }

    @Override
    public List<TagResponseDTO> findAll() {
        return repository.findAllByDeletedFalse().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public TagResponseDTO updateTag(Long id, TagRequestDTO request) {
        Tag tag = repository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Tag não encontrada"));

        mapper.updateEntityFromDto(request, tag);
        return mapper.toDTO(repository.save(tag));
    }

    @Override
    @Transactional
    public void deleteTag(Long id) {
        Tag tag = repository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new RuntimeException("Tag não encontrada"));

        tag.setDeleted(true);
        repository.save(tag);
    }
}