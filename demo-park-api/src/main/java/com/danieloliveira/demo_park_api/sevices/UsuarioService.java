package com.danieloliveira.demo_park_api.sevices;

import com.danieloliveira.demo_park_api.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

// serve para o lombok criar um metodo contrutor com a variável UsuárioRepository como parte do metodo construtor, fazendo injeção de dependecia
@RequiredArgsConstructor
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
}
