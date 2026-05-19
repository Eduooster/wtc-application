package org.wtc.application;



import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.wtc.application.integration.controller.BaseIntegrationTest;

import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class SegmentIntegrationTest extends BaseIntegrationTest {

    @Test
    public void deveCriarEAssociarSegmentoComSucesso() throws Exception {


        Map<String, String> segmentPayload = Map.of(
                "name", "Segmento VIP 2026",
                "description", "Clientes com alto engajamento"
        );

        MvcResult segmentResult = mockMvc.perform(post("/segments")
                        .header("Authorization", operatorToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(segmentPayload)))
                .andExpect(status().isCreated())
                .andReturn();


        String responseBody = segmentResult.getResponse().getContentAsString();
        Map<?, ?> responseMap = objectMapper.readValue(responseBody, Map.class);
        String segmentId = responseMap.get("id").toString();


        // --- PASSO 2: Associar o Segmento ao Cliente ---
        // ⚠️ IMPORTANTE: Como o ID do cliente/user é dinâmico, aqui usei o ID "1" fixo para ilustrar.
        // Se você souber o ID do cliente criado na base, basta passar aqui.
        String clientId = "1";

        Map<String, List<String>> associationPayload = Map.of(
                "segmentIds", List.of(segmentId)
        );

        mockMvc.perform(post("/clients/" + clientId + "/segments")
                        .header("Authorization", operatorToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(associationPayload)))
                .andExpect(status().isOk());



        String userId = "1";

        mockMvc.perform(post("/user/" + userId + "/segments")
                        .header("Authorization", operatorToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(associationPayload)))
                .andExpect(status().isOk());
    }
}