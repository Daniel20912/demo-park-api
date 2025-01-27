package com.danieloliveira.demo_park_api.web.controllers;

import com.danieloliveira.demo_park_api.jwt.JwtToken;
import com.danieloliveira.demo_park_api.jwt.JwtUserDetailsService;
import com.danieloliveira.demo_park_api.web.dto.UsuarioLoginDTO;
import com.danieloliveira.demo_park_api.web.dto.UsuarioResponseDTO;
import com.danieloliveira.demo_park_api.web.exceptions.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Tag(name = "Autenticação", description = "Recurso para proceder com a autenticação na API")
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
// Classe que vai receber o processo de autenticação.
// É por essa classe que o usuário vai enviar seu login e senha para tentar autenticar na aplicação e, se tiver sucesso, será gerado o token de autenticação.
public class AutenticacaoController {

    private final JwtUserDetailsService detailsService;
    private final AuthenticationManager authenticationManager;

    @Operation(summary = "Autenticar na API", description = "Recurso autenticação na API", responses = {
            @ApiResponse(responseCode = "200", description = "Autenticação realizada com sucesso e retorno de um bearer token", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Credenciais inválidas", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "422", description = "Campo(s) inválido(s)", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))})
    @PostMapping("/auth")
    public ResponseEntity<?> autenticar(@RequestBody @Valid UsuarioLoginDTO dto, HttpServletRequest request) {
        log.info("Processo de autenticação pelo login {}", dto.getUsername());
        try {
            // Classe pega o usuário e senha e verifica se existe um usuário no banco de dados com essa senha.
            // Se existir, ele retorna esse objeto.
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());

            // Se na linha de cima for retornado um objeto, ele será adicionado no contexto de autenticação do Spring pelo método authenticate.
            authenticationManager.authenticate(authenticationToken);

            // Gera o token a partir do username do usuário.
            JwtToken token = detailsService.getTokenAuthenticated(dto.getUsername());

            // Responde à requisição com o token gerado.
            return ResponseEntity.ok(token);
        } catch (AuthenticationException ex) {
            // Caso não seja encontrado um usuário com esse nome e senha no banco de dados, ele vai cair nesse catch e lançar um Bad Request.
            log.warn("Bad Credentials from username '{}'", dto.getUsername());
        }
        // Retorna uma resposta indicando que as credenciais fornecidas são inválidas.
        return ResponseEntity
                .badRequest()
                .body(new ErrorMessage(request, HttpStatus.BAD_REQUEST, "Credenciais Inválidas"));
    }
}
