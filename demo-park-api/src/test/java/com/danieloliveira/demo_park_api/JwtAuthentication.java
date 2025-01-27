package com.danieloliveira.demo_park_api;

import com.danieloliveira.demo_park_api.jwt.JwtToken;
import com.danieloliveira.demo_park_api.web.dto.UsuarioLoginDTO;
import org.springframework.http.HttpHeaders;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.function.Consumer;

public class JwtAuthentication {

    // metodo de autenticação para os testes
    public static Consumer<HttpHeaders> getHeaderAuthorization(WebTestClient client, String username, String password) {

        String token = client
                .post()
                .uri("/api/v1/auth")
                .bodyValue(new UsuarioLoginDTO(username, password))
                .exchange()
                .expectStatus().isOk()
                .expectBody(JwtToken.class)
                .returnResult().getResponseBody().getToken();

        return httpHeaders -> httpHeaders.add(HttpHeaders.AUTHORIZATION, "Bearer " + token);

    }
}
