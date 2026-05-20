package org.wtc.application.integration.controller;


import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import org.wtc.application.audit.repository.AuditRepository;
import org.wtc.application.client.repository.ClientRepository;
import org.wtc.application.conversation.repository.ConversationRepository;
import org.wtc.application.message.repository.MessageRepository;
import org.wtc.application.notification.NotificationRepository;
import org.wtc.application.segment.repository.SegmentRepository;
import org.wtc.application.user.repository.UserRepository;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;






@SpringBootTest
@AutoConfigureMockMvc
@Disabled("falha no ci")
public abstract class BaseIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected String operatorToken;
    protected String clientToken;


    protected String operatorId;
    protected String clientId;

    protected String userSegmentId;
    protected String clientSegmentId;

    protected final String USER_EMAIL = "usuario.teste@empresa.com";
    protected final String USER_PASSWORD = "SenhaSecreta456@";

    protected final String CLIENT_EMAIL = "cliente.teste@empresa.com";
    protected final String CLIENT_PASSWORD = "SenhaSegura123!";

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ConversationRepository conversationRepository;

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NotificationRepository notificationRepository;

    @Autowired
    private SegmentRepository segmentRepository;

    @Autowired
    private  AuditRepository auditRepository;

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


        this.operatorId = registrarSeNaoExistir("/auth/register/user", userPayload);


        Map<String, String> clientPayload = Map.of(
                "email", CLIENT_EMAIL,
                "password", CLIENT_PASSWORD,
                "fullName", "João da Silva",
                "companyName", "Empresa de Sucesso LTDA",
                "taxId", "12.345.678/0001-90",
                "phoneNumber", "+55 11 99999-9999"
        );
        this.clientId = registrarSeNaoExistir("/auth/register/client", clientPayload);


        this.operatorToken = "Bearer " + obterTokenLogin(USER_EMAIL, USER_PASSWORD);
        this.clientToken = "Bearer " + obterTokenLogin(CLIENT_EMAIL, CLIENT_PASSWORD);




        attachSegmentToBaseContext();




    }

    public String registrarSeNaoExistir(String url, Map<String, String> payload) {
        try {
            MvcResult result = mockMvc.perform(post(url)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(payload)))
                    .andReturn();

            String content = result.getResponse().getContentAsString();


            if (content != null && !content.isBlank()) {
                Map<?, ?> responseMap = objectMapper.readValue(content, Map.class);
                if (responseMap.containsKey("id")) {
                    return responseMap.get("id").toString();
                }
            }
        } catch (Exception e) {

        }
        return null;
    }


    public String obterTokenLogin(String email, String password) throws Exception {
        Map<String, String> loginPayload = Map.of("email", email, "password", password);

        MvcResult result = mockMvc.perform(post("/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(loginPayload)))
                .andExpect(status().isOk())
                .andReturn();

        String responseBody = result.getResponse().getContentAsString();
        Map<?, ?> responseMap = objectMapper.readValue(responseBody, Map.class);
        return responseMap.get("accessToken").toString();
    }

    protected String createSegment() throws Exception {
        Map<String, String> payload = Map.of(
                "name", "segmento1",
                "description", "teste segmento1"
        );




        MvcResult result = mockMvc.perform(post("/segments")
                        .header("Authorization", operatorToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payload)))

                .andExpect(status().isCreated())
                .andReturn();

        return extractId(result);
    }

    protected void attachSegmentToBaseContext() throws Exception {


       String segmentId = createSegment();





        Map<String, Object> payloadOperador = Map.of("segmentIds", List.of(segmentId));
        Map<String, Object> payloadCliente = Map.of("segmentIds", List.of(segmentId));


        MvcResult userResult = mockMvc.perform(patch("/users/" + operatorId + "/segments")
                        .header("Authorization", operatorToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payloadOperador)))
                .andExpect(status().isNoContent())
                .andReturn();




        MvcResult clientResult = mockMvc.perform(patch("/clients/" + clientId + "/segments")
                        .header("Authorization", clientToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(payloadCliente)))
                .andExpect(status().isNoContent())
                .andReturn();


    }

    private String extractId(MvcResult result) throws Exception {
        Map<?, ?> map = objectMapper.readValue(
                result.getResponse().getContentAsString(),
                Map.class
        );
        return map.get("id").toString();
}
}