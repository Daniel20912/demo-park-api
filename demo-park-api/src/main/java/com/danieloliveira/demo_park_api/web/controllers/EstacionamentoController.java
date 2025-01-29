package com.danieloliveira.demo_park_api.web.controllers;

import com.danieloliveira.demo_park_api.entities.ClienteVaga;
import com.danieloliveira.demo_park_api.sevices.EstacionamentoService;
import com.danieloliveira.demo_park_api.web.dto.EstacionamentoCreateDTO;
import com.danieloliveira.demo_park_api.web.dto.EstacionamentoResponseDTO;
import com.danieloliveira.demo_park_api.web.dto.mapper.ClienteVagaMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/estacionamentos")
public class EstacionamentoController {

    private final EstacionamentoService estacionamentoService;

    @PostMapping("/check-in")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EstacionamentoResponseDTO> checkin(@RequestBody @Valid EstacionamentoCreateDTO dto) {
        ClienteVaga clienteVaga = ClienteVagaMapper.toClienteVaga(dto);
        estacionamentoService.checkIn(clienteVaga);

        EstacionamentoResponseDTO responseDTO = ClienteVagaMapper.toDto(clienteVaga);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri() // recupera a URI que foi usada para acessar o recurso, no caso a "api/v1/vagas"
                .path("/{recibo}") // concatena nessa URI o código de acesso para essa vaga
                .buildAndExpand(clienteVaga.getRecibo())
                .toUri(); // transforma essa operação em um objeto do tipo URI

        return ResponseEntity.created(location).body(responseDTO);

    }
}
