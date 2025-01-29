package com.danieloliveira.demo_park_api.web.controllers;


import com.danieloliveira.demo_park_api.entities.Vaga;
import com.danieloliveira.demo_park_api.sevices.VagaService;
import com.danieloliveira.demo_park_api.web.dto.VagaCreateDTO;
import com.danieloliveira.demo_park_api.web.dto.VagaResponseDTO;
import com.danieloliveira.demo_park_api.web.dto.mapper.VagaMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/v1/vagas")
public class VagaController {

    private final VagaService vagaService;



    /*
        Ao trabalhar com recursos do tipo create, existem tres formas de retornar uma resposta:
        1. Retorar o corpo com aquilo que foi criado
        2. Retornar no cabeçalho da resposta, um cabeçalho chamado Location contendo uma URI de acesso ao recurso que foi criado\
        3. Retornar o corpo mais o cabeçalho Location
     */

    // nessa operação será retornado o cabeçalho
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> create(@RequestBody @Valid VagaCreateDTO dto){
        Vaga vaga = VagaMapper.toVaga(dto);
        vagaService.salvar(vaga);

        // criação da URI que será incluida no cabeçalho location
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequestUri() // recupera a URI que foi usada para acessar o recurso, no caso a "api/v1/vagas"
                .path("/{codigo}") // concatena nessa URI o código de acesso para essa vaga
                .buildAndExpand(vaga.getCodigo())
                .toUri(); // transforma essa operação em um objeto do tipo URI

        return ResponseEntity.created(location).build();
    }


    // metodo para accesar vagas
    @GetMapping("/{codigo}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<VagaResponseDTO> getByCodigo(@PathVariable String codigo){
        Vaga vaga = vagaService.buscarPorCodigo(codigo);

        return ResponseEntity.ok(VagaMapper.toDTO(vaga));
    }
}
