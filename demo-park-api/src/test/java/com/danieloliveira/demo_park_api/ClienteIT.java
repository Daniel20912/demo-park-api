package com.danieloliveira.demo_park_api;


import com.danieloliveira.demo_park_api.web.dto.ClienteCreateDTO;
import com.danieloliveira.demo_park_api.web.dto.ClienteResponseDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

// essa annotation indica que a classe será de testes
/*
    RANDOM_PORT faz com que o tomcat seja executado em uma porta de maneira randomica,
    pois às vezes vários testes serão esxecutados ao mesmo tempo, e não pode haver conflitos de portas
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)

// essa annotation importa os scripts sql criados e diz quando eles devem ser excutados
@Sql(scripts = "/clientes/clientes-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
// executa o script de insert antes de executar o teste
@Sql(scripts = "/clientes/clientes-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
// executa o script de delete após finalizar o teste
public class ClienteIT {

    @Autowired
    private WebTestClient testClient;

    @Test
    public void CriarCliente_ComDadosValidos_RetornarClienteComStatus201  () {
        ClienteResponseDTO responseBody = testClient
                .post()
                .uri("/api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "toby@gmail.com", "123456"))
                .bodyValue(new ClienteCreateDTO("Tobias Ferreira", "55222387046"))
                .exchange()
                .expectStatus().isCreated()
                .expectBody(ClienteResponseDTO.class)
                .returnResult().getResponseBody();


        // import org.assertj.core.api.Assertions;
        // verificações do retorno da requisição
        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getId()).isNotNull();
        Assertions.assertThat(responseBody.getNome()).isEqualTo("Tobias Ferreira");
        Assertions.assertThat(responseBody.getCpf()).isEqualTo("55222387046");
    }

}
