package com.danieloliveira.demo_park_api.sevices;

import com.danieloliveira.demo_park_api.entities.Usuario;
import com.danieloliveira.demo_park_api.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// serve para o lombok criar um metodo contrutor com a variável UsuárioRepository como parte do metodo construtor, fazendo injeção de dependecia
@RequiredArgsConstructor
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Transactional // indica que é o Spring que deve tomar conta dos recursos de abrir, gerenciar e fechar as transações
    public Usuario salvar(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }


    /*
    Quando é feita a busca de um objeto usando buscarPorId(id), o método findById do JPA retorna uma entidade gerenciada pelo Hibernate.
    Isso significa que a instância retornada está associada ao Contexto de Persistência.
    Qualquer modificação feita nessa entidade gerenciada será automaticamente detectada e sincronizada com o banco de dados durante o commit da transação.
     */
    @Transactional
    public Usuario editarSenha(Long id, String password) {
        Usuario user = buscarPorId(id);
        user.setPassword(password);
        return user;
    }

    @Transactional(readOnly = true)
    public List<Usuario> buscarTodos() {
        return usuarioRepository.findAll();
    }
}
