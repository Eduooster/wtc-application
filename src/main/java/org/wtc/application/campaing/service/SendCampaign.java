package org.wtc.application.campaing.service;

import com.google.firebase.internal.FirebaseService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.el.util.MessageFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wtc.application.audit.repository.AuditRepository;
import org.wtc.application.auth.entity.AuthenticableUser;
import org.wtc.application.campaing.dto.CampaignNotificationEvent;
import org.wtc.application.campaing.entity.Campaign;
import org.wtc.application.campaing.enums.CampaignStatus;
import org.springframework.context.ApplicationEventPublisher;
import org.wtc.application.campaing.repository.CampaignRepository;
import org.wtc.application.client.entity.Client;
import org.wtc.application.client.repository.ClientRepository;
import org.wtc.application.conversation.entity.Conversation;
import org.wtc.application.conversation.enums.ConversationOrigin;
import org.wtc.application.conversation.enums.ConversationStatus;
import org.wtc.application.conversation.enums.ConversationTypeOrigin;
import org.wtc.application.conversation.repository.ConversationRepository;
import org.wtc.application.conversation.service.ConversationResolverService;
import org.wtc.application.integration.fireBase.FireBaseService;
import org.wtc.application.integration.fireBase.FirebaseNotificationService;
import org.wtc.application.message.entitity.Message;
import org.wtc.application.message.enums.MessageStatus;
import org.wtc.application.message.enums.MessageType;
import org.wtc.application.message.enums.ParticipantType;
import org.wtc.application.message.repository.MessageRepository;
import org.wtc.application.participant.Participant;
import org.wtc.application.participant.ParticipantRepository;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class SendCampaign {

    private final CampaignRepository campaignRepository;
    private final ClientRepository clientRepository;
    private final MessageRepository messageRepository;
    private final ConversationResolverService conversationResolverService;
    private final ParticipantRepository participantRepository;
    private final ApplicationEventPublisher applicationEventPublisher;
    private final AuditRepository auditRepository;


    @Transactional
    public void sendCampaign(Long campaignId) {
        Campaign campaign = findPendingCampaign(campaignId);
        Participant systemParticipant = findSystemParticipant();
        List<Client> targetClients = clientRepository.findAllBySegments(campaign.getTargetSegment());

        for (Client client : targetClients) {

            processCampaignForClient(campaign, client, systemParticipant);

            publishCampaignNotificationEvent(client,campaign);

        }

        finalizeCampaign(campaign);
    }

    private Campaign findPendingCampaign(Long campaignId) {
        Campaign campaign = campaignRepository.findById(campaignId)
                .orElseThrow(() -> new EntityNotFoundException("Campaign not found"));

        if ( campaign.getStatus() != CampaignStatus.PENDING &&
                campaign.getStatus() != CampaignStatus.SCHEDULED) {
            throw new RuntimeException("Campaign already processed");
        }
        return campaign;
    }

    private Participant findSystemParticipant() {
        return participantRepository.findByParticipantType(ParticipantType.SYSTEM)
                .orElseThrow(() -> new RuntimeException("System participant not found"));
    }


    private void processCampaignForClient(Campaign campaign, Client client, Participant systemParticipant) {
        Conversation conversation = getOrCreateConversation(client.getParticipant(), campaign.getTitle());

        createAndSaveMessage(campaign, conversation, systemParticipant);
    }


    private Conversation getOrCreateConversation(Participant clientParticipant, String campaignTitle) {
        Set<Participant> participants = Set.of(clientParticipant);
        return conversationResolverService.getOrCreateActiveConversation(
                participants,
                ConversationOrigin.CAMPAIGN,
                ConversationStatus.WAITING_OPERATOR,
                ConversationTypeOrigin.CAMPAIGN,
                campaignTitle,
                null
        );
    }


    private void createAndSaveMessage(Campaign campaign, Conversation conversation, Participant systemParticipant) {
        Message message = new Message();
        message.setConversation(conversation);
        message.setContent(campaign.getContent());
        message.setSender(systemParticipant);
        message.setRead(false);
        message.setSentAt(LocalDateTime.now());
        message.setType(MessageType.CAMPAIGN);
        message.setStatus(MessageStatus.SENT);
        message.setCampaign(campaign);


        messageRepository.save(message);
    }

    private void finalizeCampaign(Campaign campaign) {
        campaign.setStatus(CampaignStatus.SENT);
        campaign.setSentAt(LocalDateTime.now());
        campaign.setSendNotification(true);


        campaignRepository.save(campaign);
    }

    private void publishCampaignNotificationEvent(Client client, Campaign campaign) {
        if (client.getFirebaseToken() != null && !client.getFirebaseToken().isEmpty()) {
            log.info("sending campaign notification");
            applicationEventPublisher.publishEvent(
                    new CampaignNotificationEvent(
                            client.getFirebaseToken(),
                            campaign.getTitle(),
                            campaign.getContent()
                    )
            );
        }
    }
}