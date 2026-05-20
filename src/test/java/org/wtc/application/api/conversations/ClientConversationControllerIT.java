package org.wtc.application.api.conversations;

import jakarta.transaction.TransactionScoped;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Disabled;
import org.wtc.application.conversation.dto.ClientConversationRequestDTO;
import org.wtc.application.integration.controller.BaseApiTest;




import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;


// Recomendavel rodar com o perfil TEST
class ClientConversationControllerIT extends BaseApiTest {

    @BeforeEach
    void setUp() {

        RestAssured.basePath = "/conversations";
    }

    @Test
    void deveCriarConversaComSucesso_QuandoClienteAutenticadoEDadosValidos() {
        ClientConversationRequestDTO payloadValido = new ClientConversationRequestDTO(
                "Suporte Técnico",
                "Olá, estou com problemas no meu extrato de rendimentos."
        );

        RestAssured
                .given()
                .header("Authorization", clientToken)
                .contentType(ContentType.JSON)
                .body(payloadValido)
                .log().all()
                .when()
                .post("/client")
                .then()
                .log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @Test
    void deveRetornar403_QuandoUsuarioNaoTiverRegraDeCliente() {
        ClientConversationRequestDTO payloadValido = new ClientConversationRequestDTO("Título", "Mensagem");

        RestAssured
                .given()
                .header("Authorization", operatorToken)
                .contentType(ContentType.JSON)
                .body(payloadValido)
                .log().all()
                .when()
                .post("/client")
                .then()
                .log().all()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }
}