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
}
