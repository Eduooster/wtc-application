package org.wtc.application.client.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wtc.application.client.entity.Client;
import org.wtc.application.client.repository.ClientRepository;
import org.wtc.application.integration.fireBase.DeviceTokenDto;

@Service
@RequiredArgsConstructor
public class UpdateFireBaseToken {

    private final ClientRepository clientRepository;

    @Transactional
    public void updateFirebaseToken(Long clientId, DeviceTokenDto token) {

        System.out.println("TOKEN Q CHEGOU" + token);
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new EntityNotFoundException("Client not found"));

        client.setFirebaseToken(token.token());
        clientRepository.save(client);
    }

}
