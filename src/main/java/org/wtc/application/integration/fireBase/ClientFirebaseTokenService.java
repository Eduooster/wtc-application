package org.wtc.application.integration.fireBase;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wtc.application.auth.entity.AuthenticableUser;
import org.wtc.application.client.entity.Client;
import org.wtc.application.client.repository.ClientRepository;

@Service
@RequiredArgsConstructor
public class ClientFirebaseTokenService {

    private final ClientRepository clientRepository;

    @Transactional
    public void saveToken(
            AuthenticableUser principal,
            FireBaseTokenRequestDto dto
    ) {

        Client client = clientRepository
                .findByCredentials(principal)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        client.setFirebaseToken(dto.firebaseToken());

        clientRepository.save(client);
    }
}