package org.wtc.application.client.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.wtc.application.client.ClientNotFoundException;
import org.wtc.application.client.dto.ClientRequestDTO;
import org.wtc.application.client.dto.ClientResponseDTO;
import org.springframework.stereotype.Service;
import org.wtc.application.client.entity.Client;
import org.wtc.application.client.mapper.ClientMapper;

import org.wtc.application.client.repository.ClientRepository;
import org.wtc.application.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements IClientService {

    private final ClientRepository repository;
    @Qualifier("clientMapper")
    private final ClientMapper mapper;

    @Override
    @Transactional
    public ClientResponseDTO createClient(ClientRequestDTO request) {
        Client client = mapper.toEntity(request);





        client.setActive(true);
        client.setDeleted(false);

        return mapper.toDTO(repository.save(client));
    }

    @Override
    public ClientResponseDTO findById(Long id) {
        return repository.findByIdAndDeletedFalse(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new ClientNotFoundException("Cliente com ID " + id + " não encontrado"));
    }

    @Override
    public List<ClientResponseDTO> findAll() {
        return repository.findAllByDeletedFalse().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ClientResponseDTO updateClient(Long id, ClientRequestDTO request) {
        Client client = repository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ClientNotFoundException("Cliente não encontrado para atualização"));


        mapper.updateEntityFromDto(request, client);


//        if (request.getPassword() != null && !request.getPassword().isBlank()) {
//            client.setPassword(passwordEncoder.encode(request.getPassword()));
//        }

        return mapper.toDTO(repository.save(client));
    }

    @Override
    @Transactional
    public void deleteClient(Long id) {
        Client client = repository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ClientNotFoundException("Cliente não encontrado"));

        // Soft Delete
        client.setDeleted(true);
        client.setActive(false);
        repository.save(client);
    }
}