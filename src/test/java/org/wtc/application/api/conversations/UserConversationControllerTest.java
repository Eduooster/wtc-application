package org.wtc.application.api.conversations;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.transaction.annotation.Transactional;
import org.wtc.application.integration.controller.BaseApiTest;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;



class UserConversationControllerTest extends BaseApiTest {



    @BeforeEach
    void setUp() {

        RestAssured.basePath = "/conversations";
    }

    @Test
    @DisplayName("Deve criar uma conversa com sucesso quando o usuário for um OPERATOR")
    void deveCriarConversaComSucesso() {

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("title", "Suporte Técnico - Instalação");
        requestBody.put("firstMessage", "Olá, preciso de ajuda com a configuração do módulo.");
        requestBody.put("clientId", clientId);

        given()
                .contentType(ContentType.JSON)
                .header("Authorization", operatorToken)
                .body(requestBody)
                .when()
                .post("/user")
                .then()
                .statusCode(201);
    }

    @Test
    @DisplayName("Deve retornar 400 Bad Request quando os dados obrigatórios forem inválidos")
    void deveRetornarErroValidacao() {

        Map<String, Object> invalidBody = new HashMap<>();
        invalidBody.put("title", "");
        invalidBody.put("firstMessage", " ");
        invalidBody.put("clientId", null);

        given()
                .contentType(ContentType.JSON)
                .body(invalidBody)
                .when()
                .post()
                .then()
                .statusCode(403);
    }
}
