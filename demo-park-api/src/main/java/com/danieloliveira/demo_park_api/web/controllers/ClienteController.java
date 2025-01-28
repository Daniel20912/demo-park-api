package com.danieloliveira.demo_park_api.web.controllers;


import com.danieloliveira.demo_park_api.entities.Cliente;
import com.danieloliveira.demo_park_api.jwt.JwtUserDetails;
import com.danieloliveira.demo_park_api.repositories.projection.ClienteProjection;
import com.danieloliveira.demo_park_api.sevices.ClienteService;
import com.danieloliveira.demo_park_api.sevices.UsuarioService;
import com.danieloliveira.demo_park_api.web.dto.ClienteCreateDTO;
import com.danieloliveira.demo_park_api.web.dto.ClienteResponseDTO;
import com.danieloliveira.demo_park_api.web.dto.PagebleDTO;
import com.danieloliveira.demo_park_api.web.dto.mapper.ClienteMapper;
import com.danieloliveira.demo_park_api.web.dto.mapper.PagebleMapper;
import com.danieloliveira.demo_park_api.web.exceptions.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import static io.swagger.v3.oas.annotations.enums.ParameterIn.QUERY;

@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;
    private final UsuarioService usuarioService;


    @Operation(summary = "Criar novo cliente", description = "Recurso para criar um novo cliente vinculado a um usuário cadastrado" + "Requisição exige um bearer token. \"Requisição exige um bearer token. Acesso Restrito a Role = 'CLIENTE'",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    // como o código 204 é um noContente, o schema será um Void
                    @ApiResponse(responseCode = "201", description = "Recurso criado com sucesso",
                            content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = ClienteResponseDTO.class))),
                    @ApiResponse(responseCode = "409", description = "Cliente CPF já possui cadastro no sistema",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Recurso não processado por falta de dados ou dados inválidos",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Recurso não permitido ao perfil de ADMIN",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ClienteResponseDTO> create(@RequestBody @Valid ClienteCreateDTO dto, @AuthenticationPrincipal JwtUserDetails userDetails) {
        Cliente cliente = ClienteMapper.toCliente(dto);

        // precisa vincular o cliente
        cliente.setUsuario(usuarioService.buscarPorId(userDetails.getId()));
        clienteService.salvar(cliente);
        return ResponseEntity.status(201).body(ClienteMapper.toDto(cliente));
    }


    @Operation(summary = "Localizar um cliente", description = "Recurso para localizar um cliente por um id" + "Requisição exige um bearer token. Acesso Restrito a Role = 'ADMIN'",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    // como o código 204 é um noContente, o schema será um Void
                    @ApiResponse(responseCode = "200", description = "Recurso localizado com sucesso",
                            content = @Content(mediaType = " application/json;charset=UTF-8", schema = @Schema(implementation = ClienteResponseDTO.class))),
                    @ApiResponse(responseCode = "404", description = "Cliente não encontrado",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "Recurso não permitido ao perfil de CLIENTE",
                            content = @Content(mediaType = "application/json;charset=UTF-8", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ClienteResponseDTO> getById(@PathVariable Long id) {
        Cliente cliente = clienteService.buscarPorId(id);
        return ResponseEntity.ok(ClienteMapper.toDto(cliente));
    }


    @Operation(summary = "Recuperar lista de clientes",
            description = "Requisição exige uso de um bearer token. Acesso restrito a Role='ADMIN' ",
            security = @SecurityRequirement(name = "security"),
            parameters = {
                    @Parameter(in = QUERY, name = "page",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "0")),
                            description = "Representa a página retornada"
                    ),
                    @Parameter(in = QUERY, name = "size",
                            content = @Content(schema = @Schema(type = "integer", defaultValue = "5")),
                            description = "Representa o total de elementos por página"
                    ),
                    @Parameter(in = QUERY, name = "sort", hidden = true,
                            array = @ArraySchema(schema = @Schema(type = "string", defaultValue = "nome,asc")),
                            description = "Representa a ordenação dos resultados. Aceita multiplos critérios de ordenação são suportados.")
            },
            responses = {
                    @ApiResponse(responseCode = "200", description = "Recurso recuperado com sucesso",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ClienteResponseDTO.class))
                    ),
                    @ApiResponse(responseCode = "403", description = "Recurso não permito ao perfil de CLIENTE",
                            content = @Content(mediaType = " application/json;charset=UTF-8",
                                    schema = @Schema(implementation = ErrorMessage.class))
                    )
            })
        /*
        pageable serve para o processo de paginação
        coniste em dividir um conjunto de dados em várias "páginas",
        permitindo que o usuário ou o sistema recupere e exiba uma parte dos dados por vez,
        em vez de carregar tudo de uma vez
        não é uma boa prática retornar uma lista
        deve-se usar um pageble ou criar o próprio objeto de paginação
         */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<PagebleDTO> getAll(@Parameter(hidden = true) @PageableDefault(size = 5, sort = {"nome"}) Pageable pageable) { // sort = {"nome"} ordena os elementos pelos nomes
        Page<ClienteProjection> clientes = clienteService.buscarTodos(pageable);
        return ResponseEntity.ok(PagebleMapper.toDto(clientes));
    }
}
