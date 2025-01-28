package com.danieloliveira.demo_park_api;


import com.danieloliveira.demo_park_api.web.dto.ClienteCreateDTO;
import com.danieloliveira.demo_park_api.web.dto.ClienteResponseDTO;
import com.danieloliveira.demo_park_api.web.exceptions.ErrorMessage;
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
// executa o script de insert antes de executar o teste
@Sql(scripts = "/clientes/clientes-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
// executa o script de delete após finalizar o teste
@Sql(scripts = "/clientes/clientes-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
public class ClienteIT {

    @Autowired
    private WebTestClient testClient;

    @Test
    // teste criação de um cliente com os dados todos válidos
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


    @Test
    // teste criando um cliente com um cpf que já está cadastrado
    public void CriarCliente_ComCpfJaCadastrado_RetornarErrorMessageStatus409  () {
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "toby@gmail.com", "123456"))
                .bodyValue(new ClienteCreateDTO("Tobias Ferreira", "65456623099"))
                .exchange()
                .expectStatus().isEqualTo(409)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);

    }


    @Test
    // teste criando um cliente com os dados inválidos
    public void CriarCliente_ComDadosInvalidos_RetornarErrorMessageStatus422  () {
        // variação 1: com o nome e cpf vazio
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "toby@gmail.com", "123456"))
                .bodyValue(new ClienteCreateDTO("", ""))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);


        // variação 2: com o nome com menos de 5 digitos e com um cpf inválido
        responseBody = testClient
                .post()
                .uri("/api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "toby@gmail.com", "123456"))
                .bodyValue(new ClienteCreateDTO("Toby", "00000000000"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);


        // variação 3: com o cpf com pontuação (que api não aceita)
        responseBody = testClient
                .post()
                .uri("/api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "toby@gmail.com", "123456"))
                .bodyValue(new ClienteCreateDTO("Toby", "552.223.870-46"))
                .exchange()
                .expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);

    }


    @Test
    // teste criando um cliente com um usuário não autorizado
    public void CriarCliente_ComUsuarioNaoPermitido_RetornarErrorMessageStatus403  () {
        ErrorMessage responseBody = testClient
                .post()
                .uri("/api/v1/clientes")
                .contentType(MediaType.APPLICATION_JSON)
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
                .bodyValue(new ClienteCreateDTO("Ana Silva", "55222387046"))
                .exchange()
                .expectStatus().isEqualTo(403)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);

    }


    @Test
    // teste buscar um cliente
    public void BuscarCliente_ComIdExistentePeloAdmin_RetornarClienteComStatus200 () {
        ClienteResponseDTO responseBody = testClient
                .get()
                .uri("/api/v1/clientes/10")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
                .exchange()
                .expectStatus().isOk()
                .expectBody(ClienteResponseDTO.class)
                .returnResult().getResponseBody();


        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getId()).isEqualTo(10);
    }


    @Test
    // teste buscar um cliente que não existe
    public void BuscarCliente_ComIdInexistentePeloAdmin_RetornarStauts404 () {
        ErrorMessage responseBody = testClient
                .get()
                .uri("/api/v1/clientes/000")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "ana@gmail.com", "123456"))
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);
    }


    @Test
    // teste buscar um cliente pelo cliente
    public void BuscarCliente_ComIdInexistentePeloCliente_RetornarErrorMessageComStauts403 () {
        ErrorMessage responseBody = testClient
                .get()
                .uri("/api/v1/clientes/10")
                .headers(JwtAuthentication.getHeaderAuthorization(testClient, "joao@gmail.com", "123456"))
                .exchange()
                .expectStatus().isForbidden()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(403);
    }
}
