package com.danieloliveira.demo_park_api;

import com.danieloliveira.demo_park_api.web.dto.UsuarioCreateDTO;
import com.danieloliveira.demo_park_api.web.dto.UsuarioResponseDTO;
import com.danieloliveira.demo_park_api.web.exceptions.ErrorMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

// essa annotation indica que a classe será de testes
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT) // isso faz com que o tomcat seja executado em uma porta de maneira randomica

// essa annotation importa os scripts sql criados e diz quando eles devem ser excutados
@Sql(scripts = "/sql/usuarios/usuarios-insert.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD) // executa o script de insert antes de executar o teste
@Sql(scripts = "/sql/usuarios/usuarios-delete.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD) // executa o script de delete após finalizar o teste
public class UsuarioIT {

    @Autowired
    WebTestClient testClient;

    @Test // indica que é um metodo de teste
    // os nomes desses métoos devem ter o motivo do teste, o que vai ser testado e o que vai ser retornado
    // métodos de teste devem ser sempre públicos e void
    // testa se a criação ocorreu corretamente
    public void createUsuario_ComUsernameEPasswordValidos_RetornarUsuarioCriadoComStatus201() {
        UsuarioResponseDTO responseBody = testClient.post().uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDTO("bola@gmail.com", "123456")) // como o role não foi especificado será um cliente
                .exchange().expectStatus().isCreated()// se chegar qualquer código que não seja um 201 será lançada uma exceção
                .expectBody(UsuarioResponseDTO.class)
                .returnResult().getResponseBody();


        // import org.assertj.core.api.Assertions;
        // verificações do retorno da requisição
        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getId()).isNotNull();
        Assertions.assertThat(responseBody.getUsername()).isEqualTo("bola@gmail.com");
        Assertions.assertThat(responseBody.getRole()).isEqualTo("CLIENTE"); // como o response é um DTO ele só mostra o role como CLIENTE, não como ROLE_CLIENTE

    }


    @Test
    // testa os erros de username
    public void createUsuario_ComUsernameInvalido_RetornarErrorMessageStatus422() {
        // variação 1
        ErrorMessage responseBody = testClient.post().uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDTO("", "123456")) // como o role não foi especificado será um cliente
                .exchange().expectStatus().isEqualTo(422)// se chegar qualquer código que não seja um 201 será lançada uma exceção
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);



        // variação 2
        responseBody = testClient.post().uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDTO("bola@", "123456")) // como o role não foi especificado será um cliente
                .exchange().expectStatus().isEqualTo(422)// se chegar qualquer código que não seja um 201 será lançada uma exceção
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);


        // variação 3
        responseBody = testClient.post().uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDTO("bola@gmail", "123456")) // como o role não foi especificado será um cliente
                .exchange().expectStatus().isEqualTo(422)// se chegar qualquer código que não seja um 201 será lançada uma exceção
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);


    }


    @Test
    // testa os erros de senha
    public void createUsuario_ComPasswordInvalido_RetornarErrorMessageStatus422() {

        // variação 1
        ErrorMessage responseBody = testClient.post().uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDTO("alex@gmail.com", "")) // como o role não foi especificado será um cliente
                .exchange().expectStatus().isEqualTo(422)// se chegar qualquer código que não seja um 201 será lançada uma exceção
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);


        // variação 2
        responseBody = testClient.post().uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDTO("alex@gmail.com", "1234")) // como o role não foi especificado será um cliente
                .exchange().expectStatus().isEqualTo(422)// se chegar qualquer código que não seja um 201 será lançada uma exceção
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);


        // variação 3
        responseBody = testClient.post().uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDTO("alex@gmail.com", "12345678")) // como o role não foi especificado será um cliente
                .exchange().expectStatus().isEqualTo(422)// se chegar qualquer código que não seja um 201 será lançada uma exceção
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);


    }



    @Test
    public void createUsuario_ComUsernameRepetido_RetornarErrorMessage409() {
        // teste do erro ao inserir um usuário com um username que já exista no banco de dados
        ErrorMessage responseBody = testClient.post().uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDTO("ana@gmail.com", "123456")) // como o role não foi especificado será um cliente
                .exchange().expectStatus().isEqualTo(409)// se chegar qualquer código que não seja um 201 será lançada uma exceção
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);

    }
}
