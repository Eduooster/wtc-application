package org.wtc.application.campaing.service;



import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.wtc.application.campaing.dto.CampaignRequestDTO;
import org.wtc.application.campaing.dto.CampaignResponseDTO;

import java.util.List;

public interface ICampaignService {
    CampaignResponseDTO createCampaign(CampaignRequestDTO request);
    CampaignResponseDTO findById(Long id);
    Page<CampaignResponseDTO> findAll(Pageable pageable);
    CampaignResponseDTO updateCampaign(Long id, CampaignRequestDTO request);
    void deleteCampaign(Long id,Long userId);
}
