package com.danieloliveira.demo_park_api.web.controllers;

import com.danieloliveira.demo_park_api.entities.Usuario;
import com.danieloliveira.demo_park_api.sevices.UsuarioService;
import com.danieloliveira.demo_park_api.web.dto.UsuarioCreateDTO;
import com.danieloliveira.demo_park_api.web.dto.UsuarioResponseDTO;
import com.danieloliveira.demo_park_api.web.dto.UsuarioSenhaDTO;
import com.danieloliveira.demo_park_api.web.dto.mapper.UsuarioMapper;
import com.danieloliveira.demo_park_api.web.exceptions.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

// a annotation @Tag permite que se defina um nome e uma descrição par um conjunto de endpoints, que irá aparecer na documentação
@Tag(name = "Usuários", description = "Contém todas as operações relativas aos recursos para cadastro, edição e leitura de um usuário.")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    // a annotaion @Operation serve para descrever detalhes específicos de um endpoint na API
    // summary é um resumo do que esse recurso em específico vai fazer
    // responses tem infromações sobre a resposta do recurso
    @Operation(summary = "Criar um novo usuário", description = "Recurso para criar um novo usuário", responses = {
            @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "409", description = "Usuário email já cadastrado no sistema", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "422", description = "Recurso não processado por dados de entrada inválidos", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))})
    @PostMapping
    // a annotaition @Valid indica que o objeto passado deve ser validado
    public ResponseEntity<UsuarioResponseDTO> create(@Valid @RequestBody UsuarioCreateDTO createDTO) {
        Usuario user = usuarioService.salvar(UsuarioMapper.toUsuario(createDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toDto(user));
    }

    @Operation(summary = "Recuperar um usuário pelo Id", description = "Recuperar um usuário pelo Id", responses = {
            @ApiResponse(responseCode = "200", description = "Recurso recuperado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = UsuarioResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))})
    @GetMapping("/{id}")
    // faz com que o ADMIN possa acessar informações sobre qualquer outro usuário e o CLIENTE só possa acessar os dele mesmo
    @PreAuthorize("hasRole('ADMIN') OR (hasRole('CLIENTE') AND #id == authentication.principal.id)") // não precisa passar o "ROLE_"
    public ResponseEntity<UsuarioResponseDTO> getById(@PathVariable Long id) {
        Usuario user = usuarioService.buscarPorId(id);
        return ResponseEntity.ok().body(UsuarioMapper.toDto(user));
    }

    @Operation(summary = "Atualizar senha", description = "Atualizar senha", responses = {
            // como o código 204 é um noContente, o schema será um Void
            @ApiResponse(responseCode = "204", description = "Senha atualizada com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Void.class))),
            @ApiResponse(responseCode = "404", description = "Recurso não encontrado", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "400", description = "Senha não confere", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            @ApiResponse(responseCode = "422", description = "Campos inválidos ou mal formatados", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
    })
    // put é uma atualização completa do objeto, patch um parcial
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id, @Valid @RequestBody UsuarioSenhaDTO dto) {
        Usuario user = usuarioService.editarSenha(id, dto.getSenhaAtual(), dto.getNovaSenha(), dto.getConfirmaSenha());
        return ResponseEntity.noContent().build(); // retorna uma mensagem de sucesso sem corpo de resposta
    }

    @Operation(summary = "Lista todos os recursos", description = "Buscar todos os usuários cadastrados", responses = {
            @ApiResponse(responseCode = "200", description = "Todos os recursos listados com sucesso", content = @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UsuarioResponseDTO.class)))),})
    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> getAll() {
        List<Usuario> users = usuarioService.buscarTodos();
        return ResponseEntity.ok().body(UsuarioMapper.toListDto(users));
    }
}
