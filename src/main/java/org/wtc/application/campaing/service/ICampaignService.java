package org.wtc.application.campaing.service;



import org.wtc.application.campaing.dto.CampaignRequestDTO;
import org.wtc.application.campaing.dto.CampaignResponseDTO;

import java.util.List;

public interface ICampaignService {
    CampaignResponseDTO createCampaign(CampaignRequestDTO request);
    CampaignResponseDTO findById(Long id);
    List<CampaignResponseDTO> findAll();
    CampaignResponseDTO updateCampaign(Long id, CampaignRequestDTO request);
    void deleteCampaign(Long id);
}
