package com.danieloliveira.demo_park_api.web.controllers;


import com.danieloliveira.demo_park_api.entities.Cliente;
import com.danieloliveira.demo_park_api.jwt.JwtUserDetails;
import com.danieloliveira.demo_park_api.sevices.ClienteService;
import com.danieloliveira.demo_park_api.sevices.UsuarioService;
import com.danieloliveira.demo_park_api.web.dto.ClienteCreateDTO;
import com.danieloliveira.demo_park_api.web.dto.ClienteResponseDTO;
import com.danieloliveira.demo_park_api.web.dto.mapper.ClienteMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;
    private final UsuarioService usuarioService;

    @PostMapping
    @PreAuthorize("hasRole('CLIENTE')")
    public ResponseEntity<ClienteResponseDTO> create(@RequestBody @Valid ClienteCreateDTO dto, @AuthenticationPrincipal JwtUserDetails userDetails) {
        Cliente cliente = ClienteMapper.toCliente(dto);

        // precisa vincular o cliente
        cliente.setUsuario(usuarioService.buscarPorId(userDetails.getId()));
        clienteService.salvar(cliente);
        return ResponseEntity.status(201).body(ClienteMapper.toDto(cliente));
    }
}
