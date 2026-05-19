package org.wtc.application.integration.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.test.annotation.Commit;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.wtc.application.client.repository.ClientRepository;
import org.wtc.application.conversation.repository.ConversationRepository;
import org.wtc.application.message.repository.MessageRepository;
import org.wtc.application.notification.NotificationRepository;
import org.wtc.application.segment.repository.SegmentRepository;
import org.wtc.application.user.repository.UserRepository;

import java.util.List;
import java.util.Map
        ;import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ConversationFlowIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;



    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private  NotificationRepository notificationRepository;

    @Autowired
    private SegmentRepository segmentRepository;

    private String operatorId;
    private String clientId;


    @BeforeEach
    void setUp() throws Exception {

        notificationRepository.deleteAll();
        messageRepository.deleteAll();
        conversationRepository.deleteAll();
        clientRepository.deleteAll();
        userRepository.deleteAll();
        segmentRepository.deleteAll();

        Map<String, String> userPayload = Map.of(
                "email", USER_EMAIL,
                "password", USER_PASSWORD,
                "name", "Maria Souza",
                "department", "Tecnologia",
                "position", "Desenvolvedora"
        );

        Map<String, String> clientPayload = Map.of(
                "email", CLIENT_EMAIL,
                "password", CLIENT_PASSWORD,
                "fullName", "João da Silva",
                "companyName", "Empresa de Sucesso LTDA",
                "taxId", "12.345.678/0001-90",
                "phoneNumber", "+55 11 99999-9999"
        );


        this.operatorId = registrarSeNaoExistir("/auth/register/user", userPayload);
        this.clientId = registrarSeNaoExistir("/auth/register/client", clientPayload);



        this.operatorToken = "Bearer " + obterTokenLogin(USER_EMAIL, USER_PASSWORD);
        this.clientToken = "Bearer " + obterTokenLogin(CLIENT_EMAIL, CLIENT_PASSWORD);
    }

    @Test
    @DisplayName("Não deve permitir que operador assuma conversa se pertencer a um segmento diferente do cliente")
    void naoDevePermitirOperadorDeSegmentoDiferenteAssumirConversa() throws Exception {

        Map<String, String> segmentAPayload = Map.of(
                "name", "Suporte Premium",
                "description", "Segmento focado em clientes Black/Premium"
        );
        MvcResult segmentAResult = mockMvc.perform(post("/segments")
                        .header("Authorization", operatorToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(segmentAPayload)))
                .andExpect(status().isCreated())
                .andReturn();
        String segmentAId = objectMapper.readValue(segmentAResult.getResponse().getContentAsString(), Map.class)
                .get("id").toString();


        Map<String, String> segmentBPayload = Map.of(
                "name", "Suporte Varejo",
                "description", "Segmento focado em atendimento geral"
        );
        MvcResult segmentBResult = mockMvc.perform(post("/segments")
                        .header("Authorization", operatorToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(segmentBPayload)))
                .andExpect(status().isCreated())
                .andReturn();
        String segmentBId = objectMapper.readValue(segmentBResult.getResponse().getContentAsString(), Map.class)
                .get("id").toString();


        Map<String, List<String>> clientAssociation = Map.of("segmentIds", List.of(segmentAId));
        mockMvc.perform(patch("/clients/" + clientId+ "/segments")
                        .header("Authorization", operatorToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientAssociation)))
                .andExpect(status().isNoContent());


        Map<String, List<String>> operatorAssociation = Map.of("segmentIds", List.of(segmentBId));
        mockMvc.perform(patch("/user/" + operatorId+ "/segments")
                        .header("Authorization", operatorToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(operatorAssociation)))
                .andExpect(status().isNoContent());


        Map<String, String> conversationPayload = Map.of(
                "title", "Dúvida sobre o Segmento A",
                "firstMessage", "Cliente iniciou o chamado"
        );
        mockMvc.perform(post("/conversations/client")
                        .header("Authorization", clientToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(conversationPayload)))
                .andExpect(status().isCreated());


        MvcResult listaResult = mockMvc.perform(get("/conversations")
                        .param("page", "0")
                        .param("size", "10")
                        .param("status", "WAITING_OPERATOR")
                        .header("Authorization", operatorToken))
                .andExpect(status().isOk())
                .andReturn();

        Map<?, ?> pageResponse = objectMapper.readValue(listaResult.getResponse().getContentAsString(), Map.class);
        List<?> conversas = (List<?>) pageResponse.get("content");
        Map<?, ?> conversaCriada = (Map<?, ?>) conversas.get(0);
        String conversationId = conversaCriada.get("id").toString();


        mockMvc.perform(post("/conversations/" + conversationId + "/assign")
                        .header("Authorization", operatorToken))
                .andExpect(status().isForbidden());
    }
    @Test
    @DisplayName("Deve executar conversa iniciada por cliente")
    public void deveExecutarOFluxoCompletoDeConversaIniciadaPorClientEAtribuicao() throws Exception {


        Map<String, String> segmentPayload = Map.of(
                "name", "Suporte Premium Plus",
                "description", "Segmento para testar o fluxo de atendimento"
        );

        MvcResult segmentResult = mockMvc.perform(post("/segments")
                        .header("Authorization", operatorToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(segmentPayload)))
                .andExpect(status().isCreated())
                .andReturn();

        String segmentId = objectMapper.readValue(segmentResult.getResponse().getContentAsString(), Map.class)
                .get("id").toString();

        Map<String, List<String>> associationPayload = Map.of("segmentIds", List.of(segmentId));


        mockMvc.perform(patch("/clients/" + clientId+ "/segments")
                        .header("Authorization", operatorToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(associationPayload)))
                .andExpect(status().isNoContent());

        // Fazer a mesma coisa para o operador se o id dele também mudar:
        mockMvc.perform(patch("/user/" + this.operatorId + "/segments")
                        .header("Authorization", operatorToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(associationPayload)))
                .andExpect(status().isNoContent());


        Map<String, String> conversationPayload = Map.of(
                "title", "conversation criada por cliente",
                "firstMessage", "cliente iniciouo"
        );

        mockMvc.perform(post("/conversations/client")
                        .header("Authorization", clientToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(conversationPayload)))
                .andExpect(status().isCreated());


        MvcResult listaResult = mockMvc.perform(get("/conversations")
                        .param("page", "0")
                        .param("size", "10")
                        .param("status", "WAITING_OPERATOR")
                        .header("Authorization", operatorToken))
                .andExpect(status().isOk())
                .andReturn();


        Map<?, ?> pageResponse = objectMapper.readValue(listaResult.getResponse().getContentAsString(), Map.class);
        List<?> conversas = (List<?>) pageResponse.get("content");


        Map<?, ?> conversaCriada = (Map<?, ?>) conversas.get(0);
        String conversationId = conversaCriada.get("id").toString();


        mockMvc.perform(post("/conversations/" + conversationId + "/assign")
                        .header("Authorization", operatorToken))
                .andExpect(status().isOk());

    }

    @Test
    @DisplayName("Deve permitir que operador inicie conversa com cliente e o cliente responda")
    void devePermitirOperadorIniciarConversaEClienteResponder() throws Exception {


        Map<String, String> segmentPayload = Map.of(
                "name", "Suporte Ativo",
                "description", "Segmento para testes de fluxo iniciado pelo operador"
        );

        MvcResult segmentResult = mockMvc.perform(post("/segments")
                        .header("Authorization", operatorToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(segmentPayload)))
                .andExpect(status().isCreated())
                .andReturn();

        String segmentId = objectMapper.readValue(segmentResult.getResponse().getContentAsString(), Map.class)
                .get("id").toString();

        Map<String, List<String>> associationPayload = Map.of("segmentIds", List.of(segmentId));


        mockMvc.perform(patch("/clients/" + clientId + "/segments")
                        .header("Authorization", operatorToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(associationPayload)))
                .andExpect(status().isNoContent());


        mockMvc.perform(patch("/users/" + operatorId + "/segments")
                        .header("Authorization", operatorToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(associationPayload)))
                .andExpect(status().isNoContent());


        Map<String, Object> operatorConversationPayload = Map.of(
                "title", "Contato ativo sobre saldo",
                "firstMessage", "Olá, vimos uma instabilidade no seu saldo. Está tudo bem?",
                "clientId", clientId
        );

        mockMvc.perform(post("/conversations/user")
                        .header("Authorization", operatorToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(operatorConversationPayload)))
                .andExpect(status().isCreated());


        MvcResult listaResult = mockMvc.perform(get("/conversations")
                        .param("page", "0")
                        .param("size", "10")
                        .param("status", "IN_PROGRESS")
                        .header("Authorization", operatorToken))
                .andExpect(status().isOk())
                .andReturn();

        Map<?, ?> pageResponse = objectMapper.readValue(listaResult.getResponse().getContentAsString(), Map.class);
        List<?> conversas = (List<?>) pageResponse.get("content");

        Map<?, ?> conversaCriada = (Map<?, ?>) conversas.get(0);
        String conversationId = conversaCriada.get("id").toString();

        System.out.println("JSON DA CONVERSA COMPLETO: " + conversaCriada);

        String realRecipientId = clientId;



        Map<String, Object> clientResponsePayload = Map.of(
                "content", "testemensagem",
                "recipientId", Long.parseLong(clientId),
                "conversationId", Long.parseLong(conversationId),
                "recipientType", "OPERATOR"
        );

        mockMvc.perform(post("/messages")
                        .header("Authorization", clientToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(clientResponsePayload)))
                .andExpect(status().isCreated());


//        Map<?, ?> messageResponse = objectMapper.readValue(messageResult.getResponse().getContentAsString(), Map.class);
//
//
//        assert messageResponse.get("content").equals("testemensagem");
//        assert messageResponse.get("senderType").equals("CLIENT");   // Garante que o sistema identificou o cliente como remetente
//        assert messageResponse.get("receiverType").equals("USER");   // Ou "OPERATOR", depende de como está no seu Enum ParticipantType
//        assert messageResponse.get("read").equals(false);


    }


}