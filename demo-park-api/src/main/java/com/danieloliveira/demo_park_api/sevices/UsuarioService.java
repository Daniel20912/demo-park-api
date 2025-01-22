package com.danieloliveira.demo_park_api.sevices;

import com.danieloliveira.demo_park_api.entities.Usuario;
import com.danieloliveira.demo_park_api.exceptions.EntityNotFoundException;
import com.danieloliveira.demo_park_api.exceptions.PasswordInvalidException;
import com.danieloliveira.demo_park_api.exceptions.UsernameUniqueViolationException;
import com.danieloliveira.demo_park_api.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

// serve para o lombok criar um metodo contrutor com a variável UsuárioRepository como parte do metodo construtor, fazendo injeção de dependecia
@RequiredArgsConstructor
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;

    @Transactional // indica que é o Spring que deve tomar conta dos recursos de abrir, gerenciar e fechar as transações
    public Usuario salvar(Usuario usuario) {
        try {
            return usuarioRepository.save(usuario);
        } catch (DataIntegrityViolationException e) {
            throw new UsernameUniqueViolationException(String.format("Username '%s' já cadastrado", usuario.getUsername()));
        }
    }

    @Transactional(readOnly = true)
    public Usuario buscarPorId(Long id) {
        return usuarioRepository.findById(id).orElseThrow(() -> new EntityNotFoundException(String.format("Usuário id=%s não encontrado", id)));
    }


    /*
    Quando é feita a busca de um objeto usando buscarPorId(id), o método findById do JPA retorna uma entidade gerenciada pelo Hibernate.
    Isso significa que a instância retornada está associada ao Contexto de Persistência.
    Qualquer modificação feita nessa entidade gerenciada será automaticamente detectada e sincronizada com o banco de dados durante o commit da transação.
     */
    @Transactional
    public Usuario editarSenha(Long id, String senhaAtual, String novaSenha, String confirmaSenha) {
        if (!novaSenha.equals(confirmaSenha))
            throw new PasswordInvalidException("Nova senha não confere com a confirmação de senha.");


        Usuario user = buscarPorId(id);
        if (!user.getPassword().equals(senhaAtual)) throw new PasswordInvalidException("Sua senha não confere");

        user.setPassword(novaSenha);
        return user;
    }

    @Transactional(readOnly = true)
    public List<Usuario> buscarTodos() {
        return usuarioRepository.findAll();
    }
}
