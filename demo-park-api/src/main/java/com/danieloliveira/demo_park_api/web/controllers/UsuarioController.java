package com.danieloliveira.demo_park_api.web.controllers;

import com.danieloliveira.demo_park_api.entities.Usuario;
import com.danieloliveira.demo_park_api.sevices.UsuarioService;
import com.danieloliveira.demo_park_api.web.dto.UsuarioCreateDTO;
import com.danieloliveira.demo_park_api.web.dto.UsuarioSenhaDTO;
import com.danieloliveira.demo_park_api.web.dto.mapper.UsuarioMapper;
import com.danieloliveira.demo_park_api.web.dto.UsuarioResponseDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "api/v1/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PostMapping
    // a annotaition @Valid indica que o objeto passado deve ser validado
    public ResponseEntity<UsuarioResponseDTO> create(@Valid @RequestBody UsuarioCreateDTO createDTO) {
        Usuario user = usuarioService.salvar(UsuarioMapper.toUsuario(createDTO));
        return ResponseEntity.status(HttpStatus.CREATED).body(UsuarioMapper.toDto(user));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioResponseDTO> getById(@PathVariable Long id) {
        Usuario user = usuarioService.buscarPorId(id);
        return ResponseEntity.ok().body(UsuarioMapper.toDto(user));
    }

    // put é uma atualização completa do objeto, patch um parcial
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updatePassword(@PathVariable Long id, @Valid @RequestBody UsuarioSenhaDTO dto) {
        Usuario user = usuarioService.editarSenha(id, dto.getSenhaAtual(), dto.getNovaSenha(), dto.getConfirmaSenha());
        return ResponseEntity.noContent().build(); // retorna uma mensagem de sucesso sem corpo de resposta
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> getAll() {
        List<Usuario> users = usuarioService.buscarTodos();
        return ResponseEntity.ok().body(UsuarioMapper.toListDto(users));
    }
}
