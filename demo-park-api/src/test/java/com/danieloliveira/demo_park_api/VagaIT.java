package com.danieloliveira.demo_park_api;


import com.danieloliveira.demo_park_api.web.dto.VagaCreateDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Sql(scripts = "/vagas/vagas-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
@Sql(scripts = "/vagas/vagas-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class VagaIT {

    @Autowired
    private WebTestClient testClient;

    @Test
    // teste para criar vaga com os dados válidos
    public void criarVaga_ComDadosValidos_RetornarLocationComStatus201() {
        testClient
                .post()
                .uri("/api/v1/vagas")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
                .bodyValue(new VagaCreateDTO("A-05", "LIVRE"))
                .exchange()
                .expectStatus().isCreated()
                .expectHeader() // como esse metodo retorna um cabeçalho e não um corpo deve se usar o expectHeader
                .exists(HttpHeaders.LOCATION);
    }

    @Test
    // teste para criar vaga com um código que já existe
    public void criarVaga_ComCodigoJaExistente_RetornarErrorMessageComStatus409() {
        testClient
                .post()
                .uri("/api/v1/vagas")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
                .bodyValue(new VagaCreateDTO("A-01", "LIVRE"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody().jsonPath("status").isEqualTo("409")
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/vagas");

    }

    @Test
    // teste para criar vaga com dados vazios
    public void criarVaga_ComDadosInvalidos_RetornarErrorMessageComStatus422() {

        // variação 1: com código e status vazios
        testClient
                .post()
                .uri("/api/v1/vagas")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
                .bodyValue(new VagaCreateDTO("", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody().jsonPath("status").isEqualTo("422")
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/vagas");



        // variação 2: com código maior que 4 caracteres e status que não existe
        testClient
                .post()
                .uri("/api/v1/vagas")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
                .bodyValue(new VagaCreateDTO("A-503", "DESOCUPADO"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody().jsonPath("status").isEqualTo("422")
                .jsonPath("method").isEqualTo("POST")
                .jsonPath("path").isEqualTo("/api/v1/vagas");


    }


    @Test
    // teste para buscar vaga com um código existente
    public void buscarVaga_ComCodigoExistente_RetornarVagaComStatus200() {
        testClient
                .get()
                .uri("/api/v1/vagas/{codigo}" , "A-01")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody().jsonPath("id").isEqualTo(10)
                .jsonPath("codigo").isEqualTo("A-01")
                .jsonPath("status").isEqualTo("LIVRE");

    }


    @Test
    // teste para buscar vaga com um código inexistente
    public void buscarVaga_ComCodigoInexistente_RetornarErrorMessageComStatus404() {
        testClient
                .get()
                .uri("/api/v1/vagas/{codigo}" , "A-10")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody().jsonPath("status").isEqualTo(404)
                .jsonPath("method").isEqualTo("GET")
                .jsonPath("path").isEqualTo("/api/v1/vagas/A-10");

    }
}
