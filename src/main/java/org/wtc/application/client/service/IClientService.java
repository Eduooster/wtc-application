package org.wtc.application.client.service;




import org.wtc.application.client.dto.ClientRequestDTO;
import org.wtc.application.client.dto.ClientResponseDTO;

import java.util.List;

public interface IClientService {
    ClientResponseDTO createClient(ClientRequestDTO request);
    ClientResponseDTO findById(Long id);
    List<ClientResponseDTO> findAll();
    ClientResponseDTO updateClient(Long id, ClientRequestDTO request);
    void deleteClient(Long id);
}
