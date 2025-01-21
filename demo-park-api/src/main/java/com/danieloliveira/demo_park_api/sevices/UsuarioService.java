package com.danieloliveira.demo_park_api.sevices;

import com.danieloliveira.demo_park_api.entities.Usuario;
import com.danieloliveira.demo_park_api.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// serve para o lombok criar um metodo contrutor com a variável UsuárioRepository como parte do metodo construtor, fazendo injeção de dependecia
@RequiredArgsConstructor
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Transactional // indica que é o Spring que deve tomar conta dos recursos de abrir, gerenciar e fechar as transações
    public Usuario salvar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }
}
