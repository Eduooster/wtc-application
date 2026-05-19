package org.wtc.application.client.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.wtc.application.auth.entity.AuthenticableUser;
import org.wtc.application.client.entity.Client;
import org.wtc.application.client.repository.ClientRepository;
import org.wtc.application.tag.entity.Tag;
import org.wtc.application.tag.repository.TagRepository;
import org.wtc.application.user.dto.UpdateUserTagRequestDto;

import java.util.HashSet;
import java.util.Set;

@Service
@RequiredArgsConstructor

public class UpdateClientTagsService {
    private final ClientRepository clientRepository;

    private final TagRepository tagRepository;

    public void updateTagClient(UpdateUserTagRequestDto updateUserTagRequestDto, Long userId) {

        Client client =clientRepository.findById(userId).orElseThrow(() -> new RuntimeException("Client not found"));

        Set<Tag> tags = new HashSet<>(
                tagRepository.findAllById(updateUserTagRequestDto.tagsId())
        );

        client.setTags(tags);



    }
}
