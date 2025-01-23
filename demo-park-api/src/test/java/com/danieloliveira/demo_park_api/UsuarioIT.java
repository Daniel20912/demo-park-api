package com.danieloliveira.demo_park_api;

import com.danieloliveira.demo_park_api.web.dto.UsuarioCreateDTO;
import com.danieloliveira.demo_park_api.web.dto.UsuarioResponseDTO;
import com.danieloliveira.demo_park_api.web.dto.UsuarioSenhaDTO;
import com.danieloliveira.demo_park_api.web.exceptions.ErrorMessage;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.List;

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
        UsuarioResponseDTO responseBody = testClient.post()
                .uri("/api/v1/usuarios")
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
        ErrorMessage responseBody = testClient.post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDTO("", "123456"))
                .exchange().expectStatus().isEqualTo(422)
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
        ErrorMessage responseBody = testClient.post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDTO("alex@gmail.com", ""))
                .exchange().expectStatus().isEqualTo(422)
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
    // teste com username repetido
    public void createUsuario_ComUsernameRepetido_RetornarErrorMessage409() {
        // teste do erro ao inserir um usuário com um username que já exista no banco de dados
        ErrorMessage responseBody = testClient.post()
                .uri("/api/v1/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioCreateDTO("ana@gmail.com", "123456"))
                .exchange().expectStatus().isEqualTo(409)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(409);

    }


    @Test
    // teste busca de um usuário pelo id
    public void buscarUsuario_ComIdExistente_RetornarUsuarioComStatus200() {
        UsuarioResponseDTO responseBody = testClient.get()
                .uri("/api/v1/usuarios/100")
                .exchange().expectStatus().isOk()// se chegar qualquer código que não seja um 200 será lançada uma exceção
                .expectBody(UsuarioResponseDTO.class)
                .returnResult().getResponseBody();


        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getId()).isEqualTo(100);
        Assertions.assertThat(responseBody.getUsername()).isEqualTo("ana@gmail.com");
        Assertions.assertThat(responseBody.getRole()).isEqualTo("ADMIN"); // como o response é um DTO ele só mostra o role como CLIENTE, não como ROLE_CLIENTE

    }


    @Test
    // teste busca de um usuário pelo id que não existe
    public void buscarUsuario_ComIdInexistente_RetornarUsuarioComStatus404() {
        ErrorMessage responseBody = testClient.get()
                .uri("/api/v1/usuarios/1234")
                .exchange().expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);

    }

    @Test
    // teste da alteração de senha
    public void editarSenha_ComDadosValidos_RetornarStatus204() {
        testClient.patch()
                .uri("/api/v1/usuarios/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDTO("123456", "000000", "000000"))
                .exchange().expectStatus().isNoContent();

    }


    @Test
    // teste busca de um usuário pelo id que não existe
    public void editarSenha_ComIdInexistente_RetornarUsuarioComStatus404() {
        ErrorMessage responseBody = testClient.patch()
                .uri("/api/v1/usuarios/1234")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDTO("123456", "000000", "000000"))
                .exchange().expectStatus().isNotFound()
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(404);

    }


    @Test
    // teste edição da senha com campos inválidos
    public void editarSenha_ComCamposInvaliodos_RetornarErrorMessage422() {

        // variação 1
        ErrorMessage responseBody = testClient.patch()
                .uri("/api/v1/usuarios/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDTO("", "", ""))
                .exchange().expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);


        // variação 2
        responseBody = testClient.patch()
                .uri("/api/v1/usuarios/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDTO("12345", "12345", "12345"))
                .exchange().expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);



        // variação 3
        responseBody = testClient.patch()
                .uri("/api/v1/usuarios/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDTO("1234567", "1234567", "1234567"))
                .exchange().expectStatus().isEqualTo(422)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(422);
    }


    @Test
    // teste edição da senha quando as senhas não conferem
    public void editarSenha_ComSenhasInvalidas_RetornarErrorMessage400() {

        // variação 1
        ErrorMessage responseBody = testClient.patch()
                .uri("/api/v1/usuarios/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDTO("123456", "123456", "000000"))
                .exchange().expectStatus().isEqualTo(400)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);


        // variação 2
        responseBody = testClient.patch()
                .uri("/api/v1/usuarios/100")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new UsuarioSenhaDTO("000000", "123456", "123456"))
                .exchange().expectStatus().isEqualTo(400)
                .expectBody(ErrorMessage.class)
                .returnResult().getResponseBody();


        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.getStatus()).isEqualTo(400);


    }


    @Test
    // teste listar todos os usuários
    public void listarUsuarios_SemQualquerParametro_RetornarListaDeUsuariosCriadosRetornarStatus200() {
        List<UsuarioResponseDTO> responseBody = testClient.get()
                .uri("/api/v1/usuarios")
                .exchange().expectStatus().isOk()
                .expectBodyList(UsuarioResponseDTO.class)
                .returnResult().getResponseBody();


        Assertions.assertThat(responseBody).isNotNull();
        Assertions.assertThat(responseBody.size()).isEqualTo(3);


    }
}
