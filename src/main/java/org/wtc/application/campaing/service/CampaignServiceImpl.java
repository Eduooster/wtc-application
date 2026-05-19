package org.wtc.application.campaing.service;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.wtc.application.audit.entity.Audit;
import org.wtc.application.audit.repository.AuditRepository;
import org.wtc.application.campaing.dto.CampaignRequestDTO;
import org.wtc.application.campaing.dto.CampaignResponseDTO;
import org.springframework.stereotype.Service;
import org.wtc.application.campaing.entity.Campaign;
import org.wtc.application.campaing.exceptions.CampaignNotFoundException;
import org.wtc.application.campaing.mapper.CampaignMapper;
import org.wtc.application.campaing.repository.CampaignRepository;
import org.wtc.application.segment.entity.Segment;
import org.wtc.application.segment.repository.SegmentRepository;
import org.wtc.application.tag.entity.Tag;
import org.wtc.application.tag.repository.TagRepository;
import org.wtc.application.user.entity.User;
import org.wtc.application.user.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CampaignServiceImpl implements ICampaignService {

    private final CampaignRepository repository;
    private final UserRepository userRepository;

    private final SegmentRepository segmentRepository;
    @Qualifier("campaignMapper")
    private final CampaignMapper mapper;
    private final AuditRepository auditRepository;
    private final CampaignRepository campaignRepository;

    @Override
    @Transactional
    public CampaignResponseDTO createCampaign(CampaignRequestDTO request) {
        Campaign campaign = mapper.toEntiy(request);
        User creator = userRepository.findById(1L).get();
        Segment segment = segmentRepository.findById(1L).get();
        campaign.setCreator(creator);
        campaign.setTargetSegment(segment);
        campaign.setDeleted(false);
        return mapper.toDTO(repository.save(campaign));
    }

    @Override
    public CampaignResponseDTO findById(Long id) {
        return repository.findByIdAndDeletedFalse(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new CampaignNotFoundException("Campanha não encontrada ou excluída"));
    }

    @Override
    public Page<CampaignResponseDTO> findAll(Pageable pageable) {

        return repository.findAllByDeletedFalse(pageable)
                .map(mapper::toDTO);

    }

    @Override
    @Transactional
    public CampaignResponseDTO updateCampaign(Long id, CampaignRequestDTO request) {
        Campaign campaign = repository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new CampaignNotFoundException("Campanha não encontrada"));


        mapper.updateEntityFromDto(request, campaign);

        return mapper.toDTO(repository.save(campaign));
    }

    @Override
    @Transactional
    public void deleteCampaign(Long id,Long userId) {
        Campaign campaign = repository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new CampaignNotFoundException("Campanha não encontrada"));

        User user = userRepository.findById(userId).orElseThrow(
                ()-> new EntityNotFoundException("User not found")
        );


        campaign.setDeleted(true);

        Campaign savedCampaign=  campaignRepository.save(campaign);
        auditRepository.save(
                new Audit(
                        "CAMPAIGN_SENT",
                        "Campaign sent successfully",
                        user,
                        savedCampaign.getId(),
                        "Campaign",
                        false
                )
        );

        repository.save(savedCampaign);
    }
}